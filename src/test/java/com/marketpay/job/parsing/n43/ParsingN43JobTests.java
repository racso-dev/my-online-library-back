package com.marketpay.job.parsing.n43;

import com.marketpay.references.Transaction;
import com.marketpay.references.TransactionN43;
import com.marketpay.references.TransactionSens;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ParsingN43JobTests {

    private final String FIRSTLINE_N43_FILE = "110049150026101157911706061706062000000000000009783GROUP SUPECO MAXOR\n";
    private final String TRANSACTION_LINE = "22    1500170606170605121252000000007107340002704735                            \n";
    private final String COMISSION_LINE = "22    1500170606170605172051000000000014650002704735                            \n";
    private final String BAD_FIRST_LINE = "110049150026101157911706061706062000000000000009783\n";
    private final String SECOND_TRANSACTION_LINE = "22    1500170606170605121252000000007107340002704735                            \n";

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
        Integer contractNumber = parsingN43Job.getContractNumber(TRANSACTION_LINE);
        assertTrue(contractNumber == 2704735);
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
        TransactionSens sens = parsingN43Job.getSens(TRANSACTION_LINE);
        assertTrue(sens == TransactionSens.DEBIT);
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
        TransactionN43 firstTransaction = new TransactionN43();
        TransactionN43 secondTransaction = new TransactionN43();
        firstTransaction.setOperation_type(125);
        secondTransaction.setOperation_type(125);

        assertTrue(parsingN43Job.shouldAgrega(firstTransaction, secondTransaction));
    }

    @Test
    public void parsingShouldNotCombine() {
        TransactionN43 firstTransaction = new TransactionN43();
        TransactionN43 secondTransaction = new TransactionN43();
        firstTransaction.setOperation_type(127);
        secondTransaction.setOperation_type(127);

        assertFalse(parsingN43Job.shouldAgrega(firstTransaction, secondTransaction));
    }

    @Test
    public void combineTwoCredit() {
        TransactionN43 firstTransaction = new TransactionN43();
        firstTransaction.setNet_amount(10);
        firstTransaction.setGross_amount(25);

        TransactionN43 combinedTransaction = parsingN43Job.combineTransaction(firstTransaction, firstTransaction);
        assertEquals(combinedTransaction.getGross_amount(), 50);
        assertEquals(combinedTransaction.getNet_amount(), 20);
    }

    @Test
    public void combineShouldReturnDebitTransaction() {
        TransactionN43 firstTransaction = new TransactionN43();
        firstTransaction.setGross_amount(25);
        firstTransaction.setNet_amount(10);
        firstTransaction.setSens(TransactionSens.CREDIT);

        TransactionN43 secondTransaction = new TransactionN43();
        secondTransaction.setGross_amount(50);
        secondTransaction.setNet_amount(20);
        secondTransaction.setSens(TransactionSens.DEBIT);

        TransactionN43 combinedTransaction = parsingN43Job.combineTransaction(firstTransaction, secondTransaction);

        assertEquals(combinedTransaction.getNet_amount(), 10);
        assertEquals(combinedTransaction.getGross_amount(), 25);
        assertTrue(combinedTransaction.getSens() == TransactionSens.DEBIT);
    }

    @Test
    public void combineShouldReturnCreditTransaction() {
        TransactionN43 firstTransaction = new TransactionN43();
        firstTransaction.setGross_amount(75);
        firstTransaction.setNet_amount(30);
        firstTransaction.setSens(TransactionSens.CREDIT);

        TransactionN43 secondTransaction = new TransactionN43();
        secondTransaction.setGross_amount(50);
        secondTransaction.setNet_amount(20);
        secondTransaction.setSens(TransactionSens.DEBIT);

        TransactionN43 combinedTransaction = parsingN43Job.combineTransaction(firstTransaction, secondTransaction);

        assertEquals(combinedTransaction.getNet_amount(), 10);
        assertEquals(combinedTransaction.getGross_amount(), 25);
        assertTrue(combinedTransaction.getSens() == TransactionSens.CREDIT);
    }


}
