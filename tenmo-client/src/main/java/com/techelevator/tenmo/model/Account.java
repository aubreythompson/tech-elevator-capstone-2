package com.techelevator.tenmo.model;

import java.math.BigDecimal;

/**
 *  Account includes an ID, associated user ID, and balance.
 *
 *  Note that the relationship between accounts and users is one-to-one,
 *  so each user has exactly one account and vice versa.
 */
public class Account {

    
    private int accountId;

    private int userId;

    private BigDecimal balance;

    public Account() {}

    public Account(int accountId, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
