package com.techelevator.dao;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import com.techelevator.tenmo.exceptions.UserNotFoundException;
import com.techelevator.tenmo.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
    private JdbcAccountDao accountDao;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        userDao = new JdbcUserDao(jdbcTemplate);
        accountDao = new JdbcAccountDao(jdbcTemplate,userDao);
        sut = new JdbcTransferDao(jdbcTemplate, userDao, accountDao);
    }

    @Test
    public void getTransfer_returns_correct_transfer() {
        Transfer actual = sut.getTransferById(3001);
        Assert.assertEquals(TRANSFER_1, actual);
    }

    @Test(expected = TransferNotFoundException.class)
    public void getTransfer_throws_exception_given_invalid_user() {
        sut.getTransferById(-1);
    }

    @Test
    public void getAllTransfersForUser_returns_all_transfers() {
        List<Transfer> transfers = sut.getAllTransfersForUser(1001);

        for (Transfer transfer : transfers) {
            boolean isFrom = transfer.getAccountIdFrom()==2001;
            boolean isTo = transfer.getAccountIdTo()==2001;
            Assert.assertTrue(isFrom || isTo);
        }
        Assert.assertEquals(5, transfers.size());

        transfers = sut.getAllTransfersForUser(1002);
        for (Transfer transfer : transfers) {
            boolean isFrom = transfer.getAccountIdFrom()==2002;
            boolean isTo = transfer.getAccountIdTo()==2002;
            Assert.assertTrue(isFrom || isTo);
        }
        Assert.assertEquals(4, transfers.size());

        transfers = sut.getAllTransfersForUser(1003);
        for (Transfer transfer : transfers) {
            boolean isFrom = transfer.getAccountIdFrom()==2003;
            boolean isTo = transfer.getAccountIdTo()==2003;
            Assert.assertTrue(isFrom || isTo);
        }
        Assert.assertEquals(1, transfers.size());
    }

    @Test(expected = UserNotFoundException.class)
    public void getAllTransfersForUser_throws_exception_given_invalid_user() {
        sut.getAllTransfersForUser(1000);
    }

    @Test
    public void getTransfersForAccountByStatusId_returns_correct_transfers() {
        List<Transfer> transfers = sut.getTransfersForAccountByStatusId(2001, Transfer.transferStatuses.APPROVED);
        Assert.assertEquals(3, transfers.size());
        for (Transfer transfer : transfers) {
            Assert.assertEquals(Transfer.transferStatuses.APPROVED, transfer.getTransferStatusId());
        }

        transfers = sut.getTransfersForAccountByStatusId(2001, Transfer.transferStatuses.PENDING);
        Assert.assertEquals(1, transfers.size());
        for (Transfer transfer : transfers) {
            Assert.assertEquals(Transfer.transferStatuses.PENDING, transfer.getTransferStatusId());
        }

        transfers = sut.getTransfersForAccountByStatusId(2001, Transfer.transferStatuses.REJECTED);
        Assert.assertEquals(1, transfers.size());
        for (Transfer transfer : transfers) {
            Assert.assertEquals(Transfer.transferStatuses.REJECTED, transfer.getTransferStatusId());
        }

    }

    @Test(expected = AccountNotFoundException.class)
    public void getTransfersForAccountByStatusId_throws_exception_given_invalid_account() {
        sut.getTransfersForAccountByStatusId(-1, Transfer.transferStatuses.APPROVED);
    }

    @Test
    public void create_correctly_creates_transfer() {
        Transfer newTransfer = new Transfer();
        newTransfer.setAccountIdFrom(2001);
        newTransfer.setAccountIdTo(2002);
        newTransfer.setAmount(BigDecimal.valueOf(123.45));
        newTransfer.setTransferStatusId(Transfer.transferStatuses.APPROVED);
        newTransfer.setTransferTypeId(Transfer.transferTypes.SEND);

        try {
            Transfer retrievedTransfer = sut.create(newTransfer);
            newTransfer.setTransferId(retrievedTransfer.getTransferId());
            Assert.assertEquals(newTransfer, retrievedTransfer);
        } catch (TransferNotFoundException e) {
            Assert.fail("Failed to find created transfer");
        } catch (AccountNotFoundException e) {
            Assert.fail("Failed to find To or From account");
        }
    }

    @Test(expected = AccountNotFoundException.class)
    public void create_throws_given_invalid_account() {
        Transfer newTransfer = new Transfer();
        newTransfer.setAccountIdFrom(2001);
        newTransfer.setAccountIdTo(-1);
        newTransfer.setAmount(BigDecimal.valueOf(123.45));
        newTransfer.setTransferStatusId(Transfer.transferStatuses.APPROVED);
        newTransfer.setTransferTypeId(Transfer.transferTypes.SEND);
        sut.create(newTransfer);
    }

    @Test
    public void update_correctly_updates_status() {
        sut.update(TRANSFER_2.getTransferId(), Transfer.transferStatuses.APPROVED);
        int actual = sut.getTransferById(TRANSFER_2.getTransferId()).getTransferStatusId();
        int expected = Transfer.transferStatuses.APPROVED;
        Assert.assertEquals(expected, actual);

        sut.update(TRANSFER_2.getTransferId(), Transfer.transferStatuses.REJECTED);
        actual = sut.getTransferById(TRANSFER_2.getTransferId()).getTransferStatusId();
        expected = Transfer.transferStatuses.REJECTED;
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = TransferNotFoundException.class)
    public void update_throws_given_invalid_transfer() {
        sut.update(-1, Transfer.transferStatuses.APPROVED);
    }

}
