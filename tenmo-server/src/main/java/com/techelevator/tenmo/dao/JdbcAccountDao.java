package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    @Override
    public Account getAccountByUserId(int userId) throws AccountNotFoundException, UserNotFoundException {
        userDao.getUserById(userId); //check to make sure user exists

        Account account = null;
        String sql = "SELECT * FROM tenmo_account WHERE user_id = ?;";

        SqlRowSet rowSet;
        try {
            rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        } catch (DataAccessException e) {
            throw new AccountNotFoundException("Account for userId " + userId + " not found.");
        }

        if (rowSet.next()) {
            account = mapRowToAccount(rowSet);
        } else {
            System.out.println("getAccountByUserId failed to find account");
        }

        return account;
    }

    @Override
    public Account getAccountByAccountId(int accountId) throws AccountNotFoundException {
        Account account = null;
        String sql = "SELECT * FROM tenmo_account WHERE account_id = ?;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);

        if (rowSet.next()) {
            account = mapRowToAccount(rowSet);
        } else {
            throw new AccountNotFoundException("Account " + accountId + " not found.");
        }

        return account;
    }

    @Override
    public void update(int accountId, BigDecimal amount) throws AccountNotFoundException {
        String sql = "UPDATE tenmo_account SET balance = ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(sql, amount, accountId);
        } catch (DataAccessException e) {
            throw new AccountNotFoundException("Account " + accountId + " not found.");
        }

    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        return new Account(rowSet.getInt("account_id"), rowSet.getInt("user_id"), rowSet.getBigDecimal("balance"));
    }


}
