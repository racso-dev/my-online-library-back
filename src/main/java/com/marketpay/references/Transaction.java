package com.marketpay.references;

import java.util.Date;

public class Transaction {
    private int id;
    private Date funding_date;
    private int client_id;
    private Date trade_date;
    private String cardType;
    private TransactionSens sens;
    private int gross_amount;
    private int net_amount;
    private int contract_number;

    public int getId() {
        return id;
    }

    public void setSens(TransactionSens sens) {
        this.sens = sens;
    }

    public TransactionSens getSens() {
        return sens;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFunding_date() {
        return funding_date;
    }

    public void setFunding_date(Date funding_date) {
        this.funding_date = funding_date;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public Date getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(Date trade_date) {
        this.trade_date = trade_date;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public int getGross_amount() {
        return gross_amount;
    }

    public void setGross_amount(int gross_amount) {
        this.gross_amount = gross_amount;
    }

    public int getNet_amount() {
        return net_amount;
    }

    public void setNet_amount(int net_amount) {
        this.net_amount = net_amount;
    }

    public int getContract_number() {
        return contract_number;
    }

    public void setContract_number(int contract_number) {
        this.contract_number = contract_number;
    }
}
