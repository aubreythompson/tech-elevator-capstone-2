package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    public Account getAccountByUserId(int userId);

    public Account getAccountByAccountId(int accountId);

    public void update(int accountId, BigDecimal amount);

}
