package com.marketpay.job.parsing.n43;

import com.marketpay.MarketPayUnitTests;
import com.marketpay.persistence.entity.JobHistory;
import com.marketpay.persistence.entity.Operation;
import com.marketpay.references.JOB_STATUS;
import com.marketpay.references.OPERATION_SENS;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ParsingN43JobTests extends MarketPayUnitTests {

    private final String FIRSTLINE_N43_FILE = "110049150026101157911706061706062000000000000009783GROUP SUPECO MAXOR\n";
    private final String TRANSACTION_LINE = "22    1500170606170605121252000000007107340002704735                            \n";
    private final String COMISSION_LINE = "22    1500170606170605172051000000000014650002704735                            \n";
    private final String BAD_FIRST_LINE = "110049150026101157911706061706062000000000000009783\n";
    private String N43FILE_PATH = "src/test/resources/parsing/parsingN43File.txt";

    @Autowired
    @InjectMocks
    private ParsingN43Job parsingN43Job;

    @Test
    public void getFinancingDateTest() {
        String financingDate = parsingN43Job.getFinaningDate(FIRSTLINE_N43_FILE);
        assertEquals("170606", financingDate);
    }

    @Test
    public void getContractNumberTest() {
        String contractNumber = parsingN43Job.getContractNumber(TRANSACTION_LINE);
        assertEquals("0002704735", contractNumber);
    }

    @Test
    public void getTransactionDateTest() {
        String transactionDate = parsingN43Job.getTransactionDate(TRANSACTION_LINE);
        assertEquals("170605", transactionDate);
    }

    @Test
    public void getOperationTypeTest() {
        int operation = parsingN43Job.getOperationType(TRANSACTION_LINE);
        assertEquals(125, operation);
    }

    @Test
    public void getSensTest() {
        Integer sens = parsingN43Job.getSens(TRANSACTION_LINE);
        assertNotNull(sens);
        assertEquals(OPERATION_SENS.CREDIT.getCode(), sens.intValue());
    }

    @Test
    public void getCommisionTest() {
        int commision = parsingN43Job.getCommission(COMISSION_LINE);
        assertEquals(1465, commision);
    }

    @Test
    public void getComissionShouldFail() {
        int comision = parsingN43Job.getCommission(TRANSACTION_LINE);
        assertEquals(-1, comision);
    }

    @Test
    public void getGrossAmountTest() {
        int grossAmount = parsingN43Job.getGrossAmount(TRANSACTION_LINE);
        assertEquals(710734, grossAmount);
    }

    @Test
    public void getGrossAmountShouldFail() {
        int grossAmount = parsingN43Job.getGrossAmount(COMISSION_LINE);
        assertEquals(-1, grossAmount);
    }

    @Test
    public void parsingShouldCombine() {
        Operation firstTransaction = new Operation();
        Operation secondTransaction = new Operation();
        firstTransaction.setOperationType(125);
        secondTransaction.setOperationType(125);

        assertTrue(parsingN43Job.shouldCombine(firstTransaction, secondTransaction));
    }

    @Test
    public void parsingShouldNotCombine() {
        Operation firstTransaction = new Operation();
        Operation secondTransaction = new Operation();
        firstTransaction.setOperationType(127);
        secondTransaction.setOperationType(127);

        assertFalse(parsingN43Job.shouldCombine(firstTransaction, secondTransaction));
    }

    @Test
    public void combineTwoCredit() {
        Operation firstTransaction = new Operation();
        firstTransaction.setNetAmount(10);
        firstTransaction.setGrossAmount(25);

        Operation combinedTransaction = parsingN43Job.combineTransaction(firstTransaction, firstTransaction);
        assertEquals(50, combinedTransaction.getGrossAmount());
        assertEquals(20, combinedTransaction.getNetAmount());
    }

    @Test
    public void combineShouldReturnDebitTransaction() {
        Operation firstTransaction = new Operation();
        firstTransaction.setGrossAmount(25);
        firstTransaction.setNetAmount(10);
        firstTransaction.setSens(OPERATION_SENS.CREDIT.getCode());

        Operation secondTransaction = new Operation();
        secondTransaction.setGrossAmount(50);
        secondTransaction.setNetAmount(20);
        secondTransaction.setSens(OPERATION_SENS.DEBIT.getCode());

        Operation combinedTransaction = parsingN43Job.combineTransaction(firstTransaction, secondTransaction);

        assertEquals(10, combinedTransaction.getNetAmount());
        assertEquals(25, combinedTransaction.getGrossAmount());
        int sens = combinedTransaction.getSens();
        assertEquals(OPERATION_SENS.DEBIT.getCode(), sens);
    }

    @Test
    public void combineShouldReturnCreditTransaction() {
        Operation firstTransaction = new Operation();
        firstTransaction.setGrossAmount(75);
        firstTransaction.setNetAmount(30);
        firstTransaction.setSens(OPERATION_SENS.CREDIT.getCode());

        Operation secondTransaction = new Operation();
        secondTransaction.setGrossAmount(50);
        secondTransaction.setNetAmount(20);
        secondTransaction.setSens(OPERATION_SENS.DEBIT.getCode());

        Operation combinedTransaction = parsingN43Job.combineTransaction(firstTransaction, secondTransaction);

        assertEquals(10, combinedTransaction.getNetAmount());
        assertEquals(25, combinedTransaction.getGrossAmount());
        int sens = combinedTransaction.getSens();
        assertEquals(OPERATION_SENS.CREDIT.getCode(), sens);
    }

    @Test
    public void parsingGoodN43File() {
        JobHistory jobHistory = new JobHistory();
        jobHistory.setStatus(JOB_STATUS.IN_PROGRESS.getCode());
        try {
            parsingN43Job.parsing(N43FILE_PATH, jobHistory);
            int status = jobHistory.getStatus();
            assertEquals(JOB_STATUS.IN_PROGRESS.getCode(), status);
        } catch (IOException e) {
            fail();
        }
    }


}
