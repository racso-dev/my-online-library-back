package com.marketpay.job.parsing.n43;

import com.marketpay.job.parsing.n43.ressources.OperationN43;
import com.marketpay.job.parsing.resources.JobHistory;
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
public class ParsingN43JobTests {

    private final String FIRSTLINE_N43_FILE = "110049150026101157911706061706062000000000000009783GROUP SUPECO MAXOR\n";
    private final String TRANSACTION_LINE = "22    1500170606170605121252000000007107340002704735                            \n";
    private final String COMISSION_LINE = "22    1500170606170605172051000000000014650002704735                            \n";
    private final String BAD_FIRST_LINE = "110049150026101157911706061706062000000000000009783\n";
    private String N43FILE_PATH = "src/test/resources/parsing/parsingN43File.txt";

    @Autowired
    private ParsingN43Job parsingN43Job;

    @Test
    public void getClientNameTest() {
        String clientName = parsingN43Job.getClientName(FIRSTLINE_N43_FILE);
        assertEquals("GROUP SUPECO MAXOR", clientName);
    }

    @Test
    public void getClientShouldFail() {
        String clientName = parsingN43Job.getClientName(BAD_FIRST_LINE);
        assertEquals("", clientName);
    }

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
        assertEquals(OPERATION_SENS.CREDIT.getCode(), sens);
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
        OperationN43 firstTransaction = new OperationN43();
        OperationN43 secondTransaction = new OperationN43();
        firstTransaction.setOperationType(125);
        secondTransaction.setOperationType(125);

        assertTrue(parsingN43Job.shouldCombine(firstTransaction, secondTransaction));
    }

    @Test
    public void parsingShouldNotCombine() {
        OperationN43 firstTransaction = new OperationN43();
        OperationN43 secondTransaction = new OperationN43();
        firstTransaction.setOperationType(127);
        secondTransaction.setOperationType(127);

        assertFalse(parsingN43Job.shouldCombine(firstTransaction, secondTransaction));
    }

    @Test
    public void combineTwoCredit() {
        OperationN43 firstTransaction = new OperationN43();
        firstTransaction.setNetAmount(10);
        firstTransaction.setGrossAmount(25);

        OperationN43 combinedTransaction = parsingN43Job.combineTransaction(firstTransaction, firstTransaction);
        assertEquals(50, combinedTransaction.getGrossAmount());
        assertEquals(20, combinedTransaction.getNetAmount());
    }

    @Test
    public void combineShouldReturnDebitTransaction() {
        OperationN43 firstTransaction = new OperationN43();
        firstTransaction.setGrossAmount(25);
        firstTransaction.setNetAmount(10);
        firstTransaction.setSens(OPERATION_SENS.CREDIT.getCode());

        OperationN43 secondTransaction = new OperationN43();
        secondTransaction.setGrossAmount(50);
        secondTransaction.setNetAmount(20);
        secondTransaction.setSens(OPERATION_SENS.DEBIT.getCode());

        OperationN43 combinedTransaction = parsingN43Job.combineTransaction(firstTransaction, secondTransaction);

        assertEquals(10, combinedTransaction.getNetAmount());
        assertEquals(25, combinedTransaction.getGrossAmount());
        Integer sens = combinedTransaction.getSens();
        assertEquals(OPERATION_SENS.DEBIT.getCode(), sens);
    }

    @Test
    public void combineShouldReturnCreditTransaction() {
        OperationN43 firstTransaction = new OperationN43();
        firstTransaction.setGrossAmount(75);
        firstTransaction.setNetAmount(30);
        firstTransaction.setSens(OPERATION_SENS.CREDIT.getCode());

        OperationN43 secondTransaction = new OperationN43();
        secondTransaction.setGrossAmount(50);
        secondTransaction.setNetAmount(20);
        secondTransaction.setSens(OPERATION_SENS.DEBIT.getCode());

        OperationN43 combinedTransaction = parsingN43Job.combineTransaction(firstTransaction, secondTransaction);

        assertEquals(10, combinedTransaction.getNetAmount());
        assertEquals(25, combinedTransaction.getGrossAmount());
        Integer sens = combinedTransaction.getSens();
        assertEquals(OPERATION_SENS.CREDIT.getCode(), sens);
    }

    @Test
    public void parsingGoodN43File() {
        JobHistory jobHistory = new JobHistory();
        jobHistory.setStatus(JobStatus.IN_PROGRESS);
        try {
            parsingN43Job.parsing(N43FILE_PATH, jobHistory);
            assertEquals(JobStatus.IN_PROGRESS, jobHistory.getStatus());
        } catch (IOException e) {
            fail();
        }
    }


}
