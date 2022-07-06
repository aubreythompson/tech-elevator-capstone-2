package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountByUserId(Long userId) {
        Account account = null;
        String sql = "SELECT * tenmo_account WHERE account_id = ?;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);

        if (rowSet.next()) {
            account = mapRowToAccount(rowSet);
        } else {
            System.out.println("getAccountByUserId failed to find account");
        }

        return account;
    }



    private Account mapRowToAccount(SqlRowSet rowSet) {
        return new Account(rowSet.getInt("account_id"), rowSet.getInt("user_id"), rowSet.getBigDecimal("balance"));
    }


}
