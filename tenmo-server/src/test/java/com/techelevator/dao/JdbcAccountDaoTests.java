package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests {

    private static final User USER_1 = new User(1001, "user1", "user1", "USER");
    private static final User USER_2 = new User(1002, "user2", "user2", "USER");
    private static final User USER_3 = new User(1003, "user3", "user3", "USER");

    private static final Account ACCOUNT_1 = new Account(2001, 1001, BigDecimal.valueOf(1000.00));
    private static final Account ACCOUNT_2 = new Account(2002, 1002, BigDecimal.valueOf(500.00));
    private static final Account ACCOUNT_3 = new Account(2003, 1003, BigDecimal.valueOf(100.00));

    private JdbcAccountDao sut;
    private JdbcUserDao userDao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate,userDao);
    }

    @Test
    public void getAccountByUserId_returns_Correct_account() {
        Account actual = sut.getAccountByUserId(1001);
        Assert.assertEquals(ACCOUNT_1, actual);
        actual = sut.getAccountByUserId(1002);
        Assert.assertEquals(ACCOUNT_2, actual);
        actual = sut.getAccountByUserId(1003);
        Assert.assertEquals(ACCOUNT_3, actual);
    }

    @Test
    public void getAccountByAccountId_returns_correct_account() {
        Account actual = sut.getAccountByAccountId(2001);
        Assert.assertEquals(ACCOUNT_1, actual);
        actual = sut.getAccountByAccountId(2002);
        Assert.assertEquals(ACCOUNT_2, actual);
        actual = sut.getAccountByAccountId(2003);
        Assert.assertEquals(ACCOUNT_3, actual);
    }

    @Test //relies on getAccountByAccountId
    public void update_correctly_updates_account_balance() {
        BigDecimal expected = BigDecimal.valueOf(123.45);
        sut.update(2001, expected);
        BigDecimal actual = sut.getAccountByAccountId(2001).getBalance();
        Assert.assertEquals(expected, actual);
        expected = BigDecimal.valueOf(67.89);
        sut.update(2001, expected);
        actual = sut.getAccountByAccountId(2001).getBalance();
        Assert.assertEquals(expected, actual);
    }

}
