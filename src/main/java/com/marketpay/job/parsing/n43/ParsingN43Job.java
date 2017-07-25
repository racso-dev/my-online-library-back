package com.marketpay.job.parsing.n43;

import com.marketpay.job.parsing.ParsingJob;
import com.marketpay.job.parsing.n43.ressources.OperationN43;
import com.marketpay.persistence.entity.JobHistory;
import com.marketpay.persistence.entity.Shop;
import com.marketpay.persistence.repository.JobHistoryRepository;
import com.marketpay.persistence.repository.OperationRepository;
import com.marketpay.persistence.repository.ShopRepository;
import com.marketpay.references.JOB_STATUS;
import com.marketpay.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ParsingN43Job extends ParsingJob {

    // Identifié sur les lignes commençant par 11
    private final String BU_LINE_INFORMATION = "11";
    private final String FINANCING_DATE_REGEX = "^.{20}(\\d{6})"; // Groupe 1 format JJMMAA

    // Identifié sur les lignes commençant par 22
    private final String TRANSACTION_LINE_INFORMATION_WITH_GROSSAMOUNT = "^22.{20}12\\d{3}";
    private final String TRANSACTION_LINE_INFORMATION_WITH_COMMISION = "^22.{20}17\\d{3}";
    private final String CONTRACT_NUMBER_REGEX = "^.{42}(\\d{10})"; // Groupe 1
    private final String TRANSACTION_DATE_REGEX = "^.{16}(\\d{6})"; // Groupe 1
    private final String OPERATION_SENS_REGEX = "^.{22}12(\\d{3})(\\d{1})"; // Groupe 1 : opération Groupe 2 : Sens
    private final String GROSS_AMOUNT_REGEX = "^.{22}12\\d{3}\\d{1}(\\d{14})"; // Groupe 1
    private final String COMMISION_REGEX = "^.{22}17\\d{3}\\d{1}(\\d{14})"; // Groupe 1

    private final int UNPAID_OPERATION = 127;

    private final Logger LOGGER = LoggerFactory.getLogger(ParsingN43Job.class);

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private JobHistoryRepository jobHistoryRepository;

    @Override
    public void parsing(String filePath, JobHistory jobHistory) throws IOException {
        FileReader file = new FileReader(filePath);
        BufferedReader buffer = new BufferedReader(file);

        try {
            String line;
            ArrayList<OperationN43> operationN43List = new ArrayList<>();
            LocalDate foundingDate = null;
            while ((line = buffer.readLine()) != null) {
                if (line.startsWith(BU_LINE_INFORMATION)) {
                    String foundingDateString = getFinaningDate(line);
                    foundingDate = DateUtils.convertStringToLocalDate(DATE_FORMAT_FILE, foundingDateString);
                } else if (matchFromRegex(line, TRANSACTION_LINE_INFORMATION_WITH_GROSSAMOUNT, 0) != null) {
                    OperationN43 newOperation = new OperationN43();
                    if (foundingDate != null) {
                        newOperation.setFundingDate(foundingDate);
                    }

                    newOperation.setOperationType(getOperationType(line));
                    newOperation.setContractNumber(getContractNumber(line));
                    newOperation.setGrossAmount(getGrossAmount(line));
                    newOperation.setNetAmount(newOperation.getGrossAmount());
                    String dateString = getTransactionDate(line);
                    newOperation.setTradeDate(DateUtils.convertStringToLocalDate(DATE_FORMAT_FILE, dateString));
                    newOperation.setSens(getSens(line));
                    Optional<Shop> shopOpt = shopRepository.findByContractNumber(newOperation.getContractNumber());
                    if(shopOpt.isPresent()) {
                        newOperation.setNameShop(shopOpt.get().getName());
                        newOperation.setIdShop(shopOpt.get().getId());
                    }

                    if (!operationN43List.isEmpty()) {
                        OperationN43 lastOrder = operationN43List.get(operationN43List.size() - 1);
                        if (shouldCombine(lastOrder, newOperation)) {
                            // On agrége les transactions puis on remplace la dernière transaction par la transaction agrégée
                            newOperation = combineTransaction(lastOrder, newOperation);
                            operationN43List.remove(lastOrder);
                        }
                    }
                    operationN43List.add(newOperation);
                } else if(matchFromRegex(line, TRANSACTION_LINE_INFORMATION_WITH_COMMISION, 0) != null) {
                    Integer lastIndex = operationN43List.size() - 1;
                    OperationN43 lastOperation = operationN43List.get(lastIndex);
                    OperationN43 operation = operationN43List.get(lastIndex);
                    Integer commission = getCommission(line);
                    operation.setNetAmount(operation.getGrossAmount() + commission);
                    operationN43List.remove(lastOperation);
                    operationN43List.add(operation);
                }
            }

            for(OperationN43 operationN43: operationN43List) {
                operationRepository.save(operationN43.toOperation());
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

    public String getFinaningDate(String line) {
        return matchFromRegex(line, FINANCING_DATE_REGEX, 1);
    }

    public String getContractNumber(String line) {
        String contractNumber = matchFromRegex(line, CONTRACT_NUMBER_REGEX, 1);
        return Long.valueOf(contractNumber).toString();
    }

    public String getTransactionDate(String line) {
        return matchFromRegex(line, TRANSACTION_DATE_REGEX, 1);
    }

    public Integer getOperationType(String line) {
        String operationString = matchFromRegex(line, OPERATION_SENS_REGEX, 1);
        return convertStringToInt(operationString);
    }

    public Integer getSens(String line) {
        return convertStringToInt(matchFromRegex(line, OPERATION_SENS_REGEX, 2))%2;
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
        if (firstTransaction.getOperationType() == secondTransaction.getOperationType() && firstTransaction.getOperationType() != UNPAID_OPERATION) {
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
        LOGGER.error("Une erreur s'est produit pendant le parsing du block N43 : ", e);
        jobHistory.setStatus(JOB_STATUS.FAIL.getCode());
        jobHistory.addError(e.getMessage());
        jobHistoryRepository.save(jobHistory);
    }
}
