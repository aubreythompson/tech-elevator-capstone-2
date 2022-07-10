package com.techelevator.tenmo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;
/**
 *  Account includes an ID, associated user ID, and balance.
 *
 *  Note that the relationship between accounts and users is one-to-one,
 *  so each user has exactly one account and vice versa.
 */

public class Account {

    @NotBlank
    private int accountId;

    @NotBlank
    private int userId;

    @Positive
    private BigDecimal balance;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return accountId == account.accountId && userId == account.userId && balance.doubleValue() == account.balance.doubleValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, userId, balance.doubleValue());
    }
}
