package com.example.mybankaccounts;

public class Account {
    private String id;
    private String accountName;
    private String amount;
    private String iban;
    private String currency;

    public Account(String id, String accountName, String amount, String iban, String currency) {
        this.id = id;
        this.accountName = accountName;
        this.amount = amount;
        this.iban = iban;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAmount() {
        return amount;
    }

    public String getIban() {
        return iban;
    }

    public String getCurrency() {
        return currency;
    }
}
