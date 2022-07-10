package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/***
 * gets and updates accounts
 * note: since users and accounts are one-to-one, and
 * an account is created when a user is created, there is no need
 * for a create Account method
 */

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    /***
     * since users and accounts are one-to-one, one account is returned
     * @param userId
     * @return
     */

    @Override
    public Account getAccountByUserId(int userId) {
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
    public Account getAccountByAccountId(int accountId) {
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

    /****
     * should only update the amount for the given accountID
     * @param accountId
     * @param amount
     */
    @Override
    public void update(int accountId, BigDecimal amount) {
        getAccountByAccountId(accountId); //check to make sure account exists

        String sql = "UPDATE tenmo_account SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, amount, accountId);
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        return new Account(rowSet.getInt("account_id"), rowSet.getInt("user_id"), rowSet.getBigDecimal("balance"));
    }


}
