package com.marketpay.job.parsing.n43;

import com.marketpay.job.parsing.ParsingJob;
import com.marketpay.references.TransactionSens;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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

    @Override
    public void parsing(String filePath, Object jobHistory) throws IOException {
        FileReader file = new FileReader(filePath);
        BufferedReader buffer = new BufferedReader(file);
        String line;

        while ((line = buffer.readLine()) != null) {
            if (line.startsWith(BU_LINE_INFORMATION)) {
                getClientName(line);
                getFinaningDate(line);
            } else if (line.startsWith(TRANSACTION_LINE_INFORMATION)) {
                getContractNumber(line);
                getOperationType(line);

                getCommission(line);
                getGrossAmount(line);
                getSens(line);
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

    public String getContractNumber(String line) {
        return matchFromRegex(line, CONTRACT_NUMBER_REGEX, 1);
    }

    public String getTransactionDate(String line) {
        return matchFromRegex(line, TRANSACTION_DATE_REGEX, 1);
    }

    public String getOperationType(String line) {
        return matchFromRegex(line, OPERATION_SENS_REGEX, 1);
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

    @Override
    protected void errorBlock(Exception e, String[] block, Object jobHistory) {

    }
}
