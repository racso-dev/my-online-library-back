package com.marketpay.job.parsing.coda;

import com.marketpay.persistence.entity.JobHistory;
import com.marketpay.references.JobStatus;
import com.marketpay.references.OPERATION_SENS;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ParsingCODAJobTests {

    @Autowired
    private ParsingCODAJob parsingCODAJob;

    private String TRANSACTION_LINE_MOCK = "21000200003080461201705310001  00000000015189203105170015000003080461-003 MAE 310517                               01061700901 0\n";
    private String GROSS_AMOUNT_MOCK = "2200020000BRUT:  000000000152151  COM:  000000000000259                                                                      0 0\n";
    private String FINAL_LINE_MOCK = "9               000010000000001573190000000001573190                                                                           2\n";
    private String ACCOUNT_LINE_MOCK = "10009735047736314 EUR BE   0030000        0000000000000000310517JAGI  concept BVBA                                           009\n";
    private String CODAFILE_PATH = "src/test/resources/parsing/parsingCODAFile.txt";
    private String BAD_CODAFILE_PATH = "src/test/resources/parsing/parsingBadCODAFile.txt";

    @Test
    public void parsingCardTypeTest() {
        String cardType = parsingCODAJob.getCardType(TRANSACTION_LINE_MOCK);
        assertEquals("MAE", cardType);
    }

    @Test
    public void parsingCompteNumberTest() {
        String compteNumber = parsingCODAJob.getCompteNumber(ACCOUNT_LINE_MOCK);
        assertEquals("735047736314", compteNumber);
    }

    @Test
    public void parsingSensTest() {
        Integer sens = parsingCODAJob.getSens(TRANSACTION_LINE_MOCK);
        assertEquals(OPERATION_SENS.CREDIT.getCode(), sens);
    }

    @Test
    public void parsingContractNumberTest() {
        String contractNumber = parsingCODAJob.getContractNumber(TRANSACTION_LINE_MOCK);
        assertEquals("3080461", contractNumber);
    }

    @Test
    public void parsingNetAmountTest() {
        int netAmount = parsingCODAJob.getNetAmount(TRANSACTION_LINE_MOCK);
        assertEquals(1518920, netAmount);
    }

    @Test
    public void parsingDateTest() {
        String transactionDate = parsingCODAJob.getTransactionDate(TRANSACTION_LINE_MOCK);
        assertEquals("310517", transactionDate);
    }

    @Test
    public void parsingGrossAmountTest() {
        int grossAmount = parsingCODAJob.getGrossAmount(GROSS_AMOUNT_MOCK);
        assertEquals(152151, grossAmount);
    }

    @Test
    public void parsingTotalAmountTest() {
        int totalAmount = parsingCODAJob.getTotalAmount(FINAL_LINE_MOCK);
        assertEquals(1573190, totalAmount);
    }

    @Test
    public void parsingGoodCodaFile() {
        JobHistory jobHistory = new JobHistory();
        jobHistory.setStatus(JobStatus.IN_PROGRESS.getCode());
        try {
            parsingCODAJob.parsing(CODAFILE_PATH, jobHistory);
            Integer status = jobHistory.getStatus();
            assertEquals(JobStatus.IN_PROGRESS.getCode(), status);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void parsingBadCodaFile() {
        JobHistory jobHistory = new JobHistory();
        jobHistory.setStatus(JobStatus.IN_PROGRESS.getCode());
        try {
            parsingCODAJob.parsing(BAD_CODAFILE_PATH, jobHistory);
            Integer status = jobHistory.getStatus();
            assertEquals(JobStatus.BLOCK_FAIL.getCode(), status);
        } catch (IOException e) {
            fail();
        }
    }


}
