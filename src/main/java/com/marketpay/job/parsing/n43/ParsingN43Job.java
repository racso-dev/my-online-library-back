package com.marketpay.job.parsing.n43;

import com.marketpay.job.parsing.ParsingJob;
import com.marketpay.job.parsing.n43.ressources.OperationN43;
import com.marketpay.job.parsing.resources.JobHistory;
import com.marketpay.persistence.OperationRepository;
import com.marketpay.persistence.StoreRepository;
import com.marketpay.persistence.entity.Operation;
import com.marketpay.references.JobStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParsingN43Job extends ParsingJob {

    // Identifié sur les lignes commençant par 11
    private final String BU_LINE_INFORMATION = "11";
    private final String CLIEN_NAME_REGEX = ".{51}(.*)"; // Groupe 1
    private final String FINANCING_DATE_REGEX = "^.{20}(\\d{6})"; // Groupe 1 format JJMMAA

    // Identifié sur les lignes commençant par 22
    private final String TRANSACTION_LINE_INFORMATION = "22";
    private final String CONTRACT_NUMBER_REGEX = "^.{42}(\\d{10})"; // Groupe 1
    private final String TRANSACTION_DATE_REGEX = "^.{16}(\\d{6})"; // Groupe 1
    private final String OPERATION_SENS_REGEX = "^.{22}12(\\d{3})(\\d{1})"; // Groupe 1 : opération Groupe 2 : Sens
    private final String GROSS_AMOUNT_REGEX = "^.{22}12\\d{3}\\d{1}(\\d{14})"; // Groupe 1
    private final String COMMISION_REGEX = "^.{22}17\\d{3}\\d{1}(\\d{14})"; // Groupe 1

    private final int UNPAID_OPERATION = 127;


    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private StoreRepository storeRepository;


    @Override
    public void parsing(String filePath, JobHistory jobHistory) throws IOException {
        FileReader file = new FileReader(filePath);
        BufferedReader buffer = new BufferedReader(file);

        try {
            String line;
            ArrayList<OperationN43> operationN43s = new ArrayList<>();
            LocalDate foundingDate = null;
            while ((line = buffer.readLine()) != null) {
                OperationN43 newTransaction = new OperationN43();
                if (line.startsWith(BU_LINE_INFORMATION)) {
                    getClientName(line);
                    String foundingDateString = getFinaningDate(line);
                    foundingDate = convertStringToLocalDate("ddMMyy", foundingDateString);
                } else if (line.startsWith(TRANSACTION_LINE_INFORMATION)) {

                    if (foundingDate != null) {
                        newTransaction.setFundingDate(foundingDate);
                    }
                    newTransaction.setOperation_type(getOperationType(line));
                    newTransaction.setContractNumber(getContractNumber(line));
                    newTransaction.setGrossAmount(getGrossAmount(line));
                    newTransaction.setSens(getSens(line));
                    String storeName = storeRepository.findFirstByContractNumber(newTransaction.getContractNumber()).getName();
                    newTransaction.setNameStore(storeName);
                    if (!operationN43s.isEmpty()) {
                        OperationN43 lastOrder = operationN43s.get(operationN43s.size() - 1);
                        if (shouldCombine(lastOrder, newTransaction)) {
                            // On agrége les transactions puis on remplace la dernière transaction par la transaction agrégée
                            newTransaction = combineTransaction(lastOrder, newTransaction);
                            operationN43s.remove(lastOrder);
                        }
                    }
                    operationN43s.add(newTransaction);
                }
            }

            for(Operation operation: operationN43s) {
                operationRepository.save(operation);
            }
        } catch (Exception e) {
            if(e instanceof IOException) {
                throw e;
            } else {
                errorBlock(e, null, jobHistory);
            }
        } finally {
            buffer.close();
            file.close();
        }
    }

    public String getClientName(String firstLine) {
        return matchFromRegex(firstLine, CLIEN_NAME_REGEX, 1);
    }

    public String getFinaningDate(String line) {
        return matchFromRegex(line, FINANCING_DATE_REGEX, 1);
    }

    public String getContractNumber(String line) {
        return matchFromRegex(line, CONTRACT_NUMBER_REGEX, 1);
    }

    public String getTransactionDate(String line) {
        return matchFromRegex(line, TRANSACTION_DATE_REGEX, 1);
    }

    public Integer getOperationType(String line) {
        String operationString = matchFromRegex(line, OPERATION_SENS_REGEX, 1);
        return convertStringToInt(operationString);
    }

    public Integer getSens(String line) {
        return convertStringToInt(matchFromRegex(line, OPERATION_SENS_REGEX, 2));
    }

    public Integer getGrossAmount(String line) {
        String amount = matchFromRegex(line, GROSS_AMOUNT_REGEX, 1);
        return convertStringToInt(amount);
    }

    public Integer getCommission(String line) {
        String amount = matchFromRegex(line, COMMISION_REGEX, 1);
        return convertStringToInt(amount);
    }

    /**
     * Permet de savoir si on doit ou non agréger deux transactions
     * @param firstTransaction
     * @param secondTransaction
     * @return Bool
     */
    public Boolean shouldCombine(OperationN43 firstTransaction, OperationN43 secondTransaction) {
        if (firstTransaction.getOperation_type() == secondTransaction.getOperation_type() && firstTransaction.getOperation_type() != UNPAID_OPERATION) {
            return true;
        }
        return false;
    }

    /**
     * Permet d'agrégé deux lignes N43 qui ont le même type d'opération
     * @param firstTransaction
     * @param secondTransaction
     * @return Une seule transaction
     */
    public OperationN43 combineTransaction(OperationN43 firstTransaction, OperationN43 secondTransaction) {
        OperationN43 combinedTransaction;

        if( firstTransaction.getSens() == secondTransaction.getSens()) {
            // On ajoute les montants
            combinedTransaction = firstTransaction;
            Long combineNetAmount = firstTransaction.getNetAmount() + secondTransaction.getNetAmount();
            combinedTransaction.setNetAmount(combineNetAmount);

            Long combineGrossAmount = firstTransaction.getGrossAmount() + secondTransaction.getGrossAmount();
            combinedTransaction.setGrossAmount(combineGrossAmount);

        } else {
            // On soustrait les montants
            combinedTransaction = firstTransaction;

            Long combineNetAmount = firstTransaction.getNetAmount() - secondTransaction.getNetAmount();
            Long combineGrossAmount = firstTransaction.getGrossAmount() - secondTransaction.getGrossAmount();

            if (combineGrossAmount < 0)  {
                // On prend le sens du 2 et on remet en positif
                combinedTransaction.setSens(secondTransaction.getSens());
                combinedTransaction.setGrossAmount(combineGrossAmount * -1);
                combinedTransaction.setNetAmount(combineNetAmount * -1);
            } else {
                // On prend le sens du 1
                combinedTransaction.setNetAmount(combineNetAmount);
                combinedTransaction.setGrossAmount(combineGrossAmount);
            }
        }

        return combinedTransaction;
    }

    @Override
    protected void errorBlock(Exception e, List<String> block, JobHistory jobHistory) {
        // Si il y a une erreur sur une ligne on invalid le fichier N43
        jobHistory.setStatus(JobStatus.FAIL);
        jobHistory.addError(e.getMessage());
    }
}
