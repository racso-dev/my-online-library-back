package com.marketpay.job.parsing.n43;

import com.marketpay.job.parsing.ParsingJob;
import org.springframework.stereotype.Component;

@Component
public class ParsingN43Job extends ParsingJob {

    // Identifié sur les lignes commençant par 11
    private final String CLIEN_NAME_REGEX = ".{51}(.*)"; // Groupe 1
    private final String FINANCING_DATE_REGEX = "^.{20}(\\d{6})"; // Groupe 1 format AA/MM/JJ

    // Identifié sur les lignes commençant par 22
    private final String CONTRACT_NUMBER_REGEX = "^.{42}(\\d{10})"; // Groupe 1
    private final String TRANSACTION_DATE_REGEX = "^.{16}(\\d{6})"; // Groupe 1
    private final String COMMON_CONCEPT_REGEX = "^.{22}(\\d{2})"; // Groupe 1
    private final String OPERATION_SENS_REGEX = "^.{22}12(\\d{3})(\\d{1})"; // Groupe 1 : opération Groupe 2 : Sens
    private final String GROSS_AMOUNT_REGEX = "^.{22}12\\d{3}\\d{1}(\\d{14})"; // Groupe 1
    private final String COMMISION_REGEX = "^.{22}17\\d{3}\\d{1}(\\d{14})"; // Groupe 1

    // Identifié sur les lignes commençant par 33
    private final String TOTAL_AMOUNT_REGEX = ".{59}(\\d{14})"; // Groupe 1

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

    public String getCommonConcept(String line) {
        return matchFromRegex(line, COMMON_CONCEPT_REGEX, 1);
    }

    public String getOperationType(String line) {
        return matchFromRegex(line, OPERATION_SENS_REGEX, 1);
    }

    public String getSens(String line) {
        return matchFromRegex(line, OPERATION_SENS_REGEX, 2);
    }

    public int getGrossAmount(String line) {
        String amount = matchFromRegex(line, GROSS_AMOUNT_REGEX, 1);
        return convertStringToInt(amount);
    }

    public int getCommission(String line) {
        String amount = matchFromRegex(line, COMMISION_REGEX, 1);
        return convertStringToInt(amount);
    }

    public int getTotalAmount(String line) {
        String totalAmount = matchFromRegex(line, TOTAL_AMOUNT_REGEX, 1);
        return convertStringToInt(totalAmount);
    }

}
