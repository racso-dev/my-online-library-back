package com.marketpay.job.parsing.n43;

import com.marketpay.job.parsing.n43.ressources.OperationN43;
import com.marketpay.job.parsing.resources.JobHistory;
import com.marketpay.references.JobStatus;
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
        assertTrue(clientName.equals("GROUP SUPECO MAXOR"));
    }

    @Test
    public void getClientShouldFail() {
        String clientName = parsingN43Job.getClientName(BAD_FIRST_LINE);
        assertTrue(clientName.equals(""));
    }

    @Test
    public void getFinancingDateTest() {
        String financingDate = parsingN43Job.getFinaningDate(FIRSTLINE_N43_FILE);
        assertTrue(financingDate.equals("170606"));
    }

    @Test
    public void getContractNumberTest() {
        String contractNumber = parsingN43Job.getContractNumber(TRANSACTION_LINE);
        assertTrue(contractNumber.equals("0002704735"));
    }

    @Test
    public void getTransactionDateTest() {
        String transactionDate = parsingN43Job.getTransactionDate(TRANSACTION_LINE);
        assertTrue(transactionDate.equals("170605"));
    }

    @Test
    public void getOperationTypeTest() {
        Integer operation = parsingN43Job.getOperationType(TRANSACTION_LINE);
        assertTrue(operation.equals(125));
    }

    @Test
    public void getSensTest() {
        int sens = parsingN43Job.getSens(TRANSACTION_LINE);
        assertTrue(sens == 2);
    }

    @Test
    public void getCommisionTest() {
        int commision = parsingN43Job.getCommission(COMISSION_LINE);
        assertEquals(commision, 1465);
    }

    @Test
    public void getComissionShouldFail() {
        int comision = parsingN43Job.getCommission(TRANSACTION_LINE);
        assertEquals(comision, -1);
    }

    @Test
    public void getGrossAmountTest() {
        int grossAmount = parsingN43Job.getGrossAmount(TRANSACTION_LINE);
        assertEquals(grossAmount, 710734);
    }

    @Test
    public void getGrossAmountShouldFail() {
        int grossAmount = parsingN43Job.getGrossAmount(COMISSION_LINE);
        assertEquals(grossAmount, -1);
    }

    @Test
    public void parsingShouldCombine() {
        OperationN43 firstTransaction = new OperationN43();
        OperationN43 secondTransaction = new OperationN43();
        firstTransaction.setOperation_type(125);
        secondTransaction.setOperation_type(125);

        assertTrue(parsingN43Job.shouldCombine(firstTransaction, secondTransaction));
    }

    @Test
    public void parsingShouldNotCombine() {
        OperationN43 firstTransaction = new OperationN43();
        OperationN43 secondTransaction = new OperationN43();
        firstTransaction.setOperation_type(127);
        secondTransaction.setOperation_type(127);

        assertFalse(parsingN43Job.shouldCombine(firstTransaction, secondTransaction));
    }

    @Test
    public void combineTwoCredit() {
        OperationN43 firstTransaction = new OperationN43();
        firstTransaction.setNetAmount(10);
        firstTransaction.setGrossAmount(25);

        OperationN43 combinedTransaction = parsingN43Job.combineTransaction(firstTransaction, firstTransaction);
        assertEquals(combinedTransaction.getGrossAmount(), 50);
        assertEquals(combinedTransaction.getNetAmount(), 20);
    }

    @Test
    public void combineShouldReturnDebitTransaction() {
        OperationN43 firstTransaction = new OperationN43();
        firstTransaction.setGrossAmount(25);
        firstTransaction.setNetAmount(10);
        firstTransaction.setSens(0);

        OperationN43 secondTransaction = new OperationN43();
        secondTransaction.setGrossAmount(50);
        secondTransaction.setNetAmount(20);
        secondTransaction.setSens(1);

        OperationN43 combinedTransaction = parsingN43Job.combineTransaction(firstTransaction, secondTransaction);

        assertEquals(combinedTransaction.getNetAmount(), 10);
        assertEquals(combinedTransaction.getGrossAmount(), 25);
        assertTrue(combinedTransaction.getSens() == 1);
    }

    @Test
    public void combineShouldReturnCreditTransaction() {
        OperationN43 firstTransaction = new OperationN43();
        firstTransaction.setGrossAmount(75);
        firstTransaction.setNetAmount(30);
        firstTransaction.setSens(0);

        OperationN43 secondTransaction = new OperationN43();
        secondTransaction.setGrossAmount(50);
        secondTransaction.setNetAmount(20);
        secondTransaction.setSens(1);

        OperationN43 combinedTransaction = parsingN43Job.combineTransaction(firstTransaction, secondTransaction);

        assertEquals(combinedTransaction.getNetAmount(), 10);
        assertEquals(combinedTransaction.getGrossAmount(), 25);
        assertTrue(combinedTransaction.getSens() == 0);
    }

    @Test
    public void parsingGoodN43File() {
        JobHistory jobHistory = new JobHistory();
        jobHistory.setStatus(JobStatus.IN_PROGRESS);
        try {
            parsingN43Job.parsing(N43FILE_PATH, jobHistory);
            assertTrue(jobHistory.getStatus() == JobStatus.IN_PROGRESS);
        } catch (IOException e) {
            fail();
        }
    }


}
