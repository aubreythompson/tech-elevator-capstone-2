package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.exceptions.UserNotFoundException;

import java.math.BigDecimal;

public interface AccountDao {

    public Account getAccountByUserId(int userId) throws AccountNotFoundException, UserNotFoundException;

    public Account getAccountByAccountId(int accountId) throws AccountNotFoundException;

    public void update(int accountId, BigDecimal amount) throws AccountNotFoundException;

}
