package com.marketpay.job.parsing.n43;

import com.marketpay.job.parsing.ParsingJob;
import com.marketpay.references.Transaction;
import com.marketpay.references.TransactionN43;
import com.marketpay.references.TransactionSens;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class ParsingN43Job extends ParsingJob {

    // Identifié sur les lignes commençant par 11
    private final String BU_LINE_INFORMATION = "11";
    private final String CLIEN_NAME_REGEX = ".{51}(.*)"; // Groupe 1
    private final String FINANCING_DATE_REGEX = "^.{20}(\\d{6})"; // Groupe 1 format AA/MM/JJ

    // Identifié sur les lignes commençant par 22
    private final String TRANSACTION_LINE_INFORMATION = "22";
    private final String CONTRACT_NUMBER_REGEX = "^.{42}(\\d{10})"; // Groupe 1
    private final String TRANSACTION_DATE_REGEX = "^.{16}(\\d{6})"; // Groupe 1
    private final String OPERATION_SENS_REGEX = "^.{22}12(\\d{3})(\\d{1})"; // Groupe 1 : opération Groupe 2 : Sens
    private final String GROSS_AMOUNT_REGEX = "^.{22}12\\d{3}\\d{1}(\\d{14})"; // Groupe 1
    private final String COMMISION_REGEX = "^.{22}17\\d{3}\\d{1}(\\d{14})"; // Groupe 1

    // Identifié sur les lignes commençant par 33
    private final String END_FILE_INFORMATION = "33";
    private final String TOTAL_AMOUNT_REGEX = ".{59}(\\d{14})"; // Groupe 1

    private final int UNPAID_OPERATION = 127;

    @Override
    public void parsing(String filePath, Object jobHistory) throws IOException {
        FileReader file = new FileReader(filePath);
        BufferedReader buffer = new BufferedReader(file);
        String line;
        ArrayList<TransactionN43> transactionN43s = new ArrayList<>();

        while ((line = buffer.readLine()) != null) {
            TransactionN43 newTransaction = new TransactionN43();
            if (line.startsWith(BU_LINE_INFORMATION)) {
                getClientName(line);
                getFinaningDate(line);
            } else if (line.startsWith(TRANSACTION_LINE_INFORMATION)) {

                newTransaction.setOperation_type(getOperationType(line));
                newTransaction.setContract_number(getContractNumber(line));
                newTransaction.setGross_amount(getGrossAmount(line));
                newTransaction.setSens(getSens(line));

                if (!transactionN43s.isEmpty()) {
                    TransactionN43 lastOrder = transactionN43s.get(transactionN43s.size() - 1);
                    if (shouldAgrega(lastOrder, newTransaction)) {
                        // On agrége les transactions puis on remplace la dernière transaction par la transaction agrégée
                        newTransaction = combineTransaction(lastOrder, newTransaction);
                        transactionN43s.remove(lastOrder);
                    }
                }
                transactionN43s.add(newTransaction);
            } else if (line.startsWith(END_FILE_INFORMATION)) {
                getTotalAmount(line);
            }

            // TODO : Save result
        }

        buffer.close();
        file.close();
    }

    public String getClientName(String firstLine) {
        return matchFromRegex(firstLine, CLIEN_NAME_REGEX, 1);
    }

    public String getFinaningDate(String line) {
        return matchFromRegex(line, FINANCING_DATE_REGEX, 1);
    }

    public Integer getContractNumber(String line) {
        String contractNumber = matchFromRegex(line, CONTRACT_NUMBER_REGEX, 1);
        return convertStringToInt(contractNumber);
    }

    public String getTransactionDate(String line) {
        return matchFromRegex(line, TRANSACTION_DATE_REGEX, 1);
    }

    public Integer getOperationType(String line) {
        String operationString = matchFromRegex(line, OPERATION_SENS_REGEX, 1);
        return convertStringToInt(operationString);
    }

    public TransactionSens getSens(String line) {
        Integer sens = convertStringToInt(matchFromRegex(line, OPERATION_SENS_REGEX, 2));
        if(sens == 1) {
            return TransactionSens.CREDIT;
        } else {
            return TransactionSens.DEBIT;
        }
    }

    public Integer getGrossAmount(String line) {
        String amount = matchFromRegex(line, GROSS_AMOUNT_REGEX, 1);
        return convertStringToInt(amount);
    }

    public Integer getCommission(String line) {
        String amount = matchFromRegex(line, COMMISION_REGEX, 1);
        return convertStringToInt(amount);
    }

    public Integer getTotalAmount(String line) {
        String totalAmount = matchFromRegex(line, TOTAL_AMOUNT_REGEX, 1);
        return convertStringToInt(totalAmount);
    }

    /**
     * Permet de savoir si on doit ou non agréger deux transactions
     * @param firstTransaction
     * @param secondTransaction
     * @return Bool
     */
    public Boolean shouldAgrega(TransactionN43 firstTransaction, TransactionN43 secondTransaction) {
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
    public TransactionN43 combineTransaction(TransactionN43 firstTransaction, TransactionN43 secondTransaction) {
        TransactionN43 combinedTransaction;

        if( firstTransaction.getSens() == secondTransaction.getSens()) {
            // On ajoute les montants
            combinedTransaction = firstTransaction;
            int combineNetAmount = firstTransaction.getNet_amount() + secondTransaction.getNet_amount();
            combinedTransaction.setNet_amount(combineNetAmount);

            int combineGrossAmount = firstTransaction.getGross_amount() + secondTransaction.getGross_amount();
            combinedTransaction.setGross_amount(combineGrossAmount);

        } else {
            // On soustrait les montants
            combinedTransaction = firstTransaction;

            int combineNetAmount = firstTransaction.getNet_amount() - secondTransaction.getNet_amount();
            int combineGrossAmount = firstTransaction.getGross_amount() - secondTransaction.getGross_amount();

            if (combineGrossAmount < 0)  {
                // On prend le sens du 2 et on remet en positif
                combinedTransaction.setSens(secondTransaction.getSens());
                combinedTransaction.setGross_amount(combineGrossAmount * -1);
                combinedTransaction.setNet_amount(combineNetAmount * -1);
            } else {
                // On prend le sens du 1
                combinedTransaction.setNet_amount(combineNetAmount);
                combinedTransaction.setGross_amount(combineGrossAmount);
            }
        }

        return combinedTransaction;
    }

    @Override
    protected void errorBlock(Exception e, String[] block, Object jobHistory) {

    }
}
