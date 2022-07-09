package com.techelevator.dao;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcTransferDaoTests extends BaseDaoTests {

    private static final User USER_1 = new User(1001, "user1", "user1", "USER");
    private static final User USER_2 = new User(1002, "user2", "user2", "USER");
    private static final User USER_3 = new User(1003, "user3", "user3", "USER");

    private static final Account ACCOUNT_1 = new Account(2001, 1001, BigDecimal.valueOf(1000.00));
    private static final Account ACCOUNT_2 = new Account(2002, 1002, BigDecimal.valueOf(500.00));
    private static final Account ACCOUNT_3 = new Account(2003, 1003, BigDecimal.valueOf(100.00));

    private static final Transfer TRANSFER_1 = new Transfer(3001, 2, 2, 1001, 1002, BigDecimal.valueOf(100.00));
    private static final Transfer TRANSFER_2 = new Transfer(3002, 1, 1, 1001, 1002, BigDecimal.valueOf(100.00));
    private static final Transfer TRANSFER_3 = new Transfer(3003, 1, 2, 1001, 1002, BigDecimal.valueOf(100.00));
    private static final Transfer TRANSFER_4 = new Transfer(3004, 1, 3, 1001, 1002, BigDecimal.valueOf(100.00));
    private static final Transfer TRANSFER_5 = new Transfer(3005, 2, 2, 1001, 1003, BigDecimal.valueOf(100.00));

    private JdbcTransferDao sut;
    private JdbcUserDao userDao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        userDao = new JdbcUserDao(jdbcTemplate);
        sut = new JdbcTransferDao(jdbcTemplate, userDao);
    }





}
