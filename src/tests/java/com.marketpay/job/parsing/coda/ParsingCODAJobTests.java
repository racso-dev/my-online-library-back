package com.marketpay.job.parsing.coda;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ParsingCODAJobTests {

    @Autowired
    private ParsingCODAJob parsingCODAJob;
    private String filepath = "src/tests/resources/data/parsingFiles/coda/codaMockFile.txt";

    private String TRANSACTION_LINE_MOCK = "21000200003080461201705310001  00000000015189203105170015000003080461-003 MAE 310517                               01061700901 0\n";
    private String GROSS_AMOUNT_MOCK = "2200020000BRUT:  000000000152151  COM:  000000000000259                                                                      0 0\n";
    private String FINAL_LINE_MOCK = "9               000010000000001573190000000001573190                                                                           2\n";

    @Test
    public void parsingCardTypeTest() {
        String cardType = parsingCODAJob.getCardType(TRANSACTION_LINE_MOCK);
        assertTrue(cardType.equals("MAE"));
    }

    @Test
    public void parsingSensTest() {
        String sens = parsingCODAJob.getSens(TRANSACTION_LINE_MOCK);
        assertTrue(sens.equals("0"));
    }

    @Test
    public void parsingContractNumberTest() {
        String contractNumber = parsingCODAJob.getContractNumber(TRANSACTION_LINE_MOCK);
        assertTrue(contractNumber.equals("3080461"));
    }

    @Test
    public void parsingNetAmountTest() {
        int netAmount = parsingCODAJob.getNetAmount(TRANSACTION_LINE_MOCK);
        assertEquals(netAmount, 1518920);
    }

    @Test
    public void parsingDateTest() {
        String transactionDate = parsingCODAJob.getTransactionDate(TRANSACTION_LINE_MOCK);
        assertTrue(transactionDate.equals("310517"));
    }

    @Test
    public void parsingGrossAmountTest() {
        int grossAmount = parsingCODAJob.getGrossAmount(GROSS_AMOUNT_MOCK);
        assertEquals(grossAmount, 152151);
    }

    @Test
    public void parsingTotalAmountTest() {
        int totalAmount = parsingCODAJob.getTotalAmount(FINAL_LINE_MOCK);
        assertEquals(totalAmount, 1573190);
    }
}
