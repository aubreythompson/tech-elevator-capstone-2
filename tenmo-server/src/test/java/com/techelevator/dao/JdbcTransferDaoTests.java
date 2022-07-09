package com.techelevator.dao;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests {

    private static final User USER_1 = new User(1001, "user1", "user1", "USER");
    private static final User USER_2 = new User(1002, "user2", "user2", "USER");
    private static final User USER_3 = new User(1003, "user3", "user3", "USER");

    private static final Account ACCOUNT_1 = new Account(2001, 1001, BigDecimal.valueOf(1000.00));
    private static final Account ACCOUNT_2 = new Account(2002, 1002, BigDecimal.valueOf(500.00));
    private static final Account ACCOUNT_3 = new Account(2003, 1003, BigDecimal.valueOf(100.00));

    private static final Transfer TRANSFER_1 = new Transfer(3001, Transfer.transferTypes.SEND, Transfer.transferStatuses.APPROVED, 2001, 2002, BigDecimal.valueOf(100.00));
    private static final Transfer TRANSFER_2 = new Transfer(3002, Transfer.transferTypes.REQUEST, Transfer.transferStatuses.PENDING, 2001, 2002, BigDecimal.valueOf(100.00));
    private static final Transfer TRANSFER_3 = new Transfer(3003, Transfer.transferTypes.REQUEST, Transfer.transferStatuses.APPROVED, 2001, 2002, BigDecimal.valueOf(100.00));
    private static final Transfer TRANSFER_4 = new Transfer(3004, Transfer.transferTypes.REQUEST, Transfer.transferStatuses.REJECTED, 2001, 2002, BigDecimal.valueOf(100.00));
    private static final Transfer TRANSFER_5 = new Transfer(3005, Transfer.transferTypes.SEND, Transfer.transferStatuses.APPROVED, 2001, 2003, BigDecimal.valueOf(100.00));

    private JdbcTransferDao sut;
    private JdbcUserDao userDao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        userDao = new JdbcUserDao(jdbcTemplate);
        sut = new JdbcTransferDao(jdbcTemplate, userDao);
    }

    @Test
    public void get_transfer_returns_correct_transfer() {
        Transfer transfer = sut.getTransferById(3001);
        Assert.assertEquals(3001, transfer.getTransferId());
        Assert.assertEquals(Transfer.transferTypes.SEND, transfer.getTransferTypeId());
        Assert.assertEquals(Transfer.transferStatuses.APPROVED, transfer.getTransferStatusId());
    }
/**
    @Test(expected = DataAccessException.class)
    public void get_transfer_given_invalid_user_throws_exception() {
        sut.getTransferById(-1);
    }

    @Test
    public void get_all_transfers_for_user_returns_all_transfers() {
        List<Transfer> transfers = sut.getAllTransfersForUser(1001);
        for (Transfer transfer : transfers) {
            transfer.getAccountIdFrom()
        }
    }

    getAllTransfersForUser(int userId)
 getTransfersForAccountByStatusId(int accountId, int statusId)
 Transfer create(Transfer transfer)
 update(int transferId, int statusId)

*/
}
