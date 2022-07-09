package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests {

    private final Account ACCOUNT_1 = new Account(2001, 1001, BigDecimal.valueOf(1000.00));
    private final Account ACCOUNT_2 = new Account(2002, 1002, BigDecimal.valueOf(500.00));
    private final Account ACCOUNT_3 = new Account(2003, 1003, BigDecimal.valueOf(100.00));

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }




}
