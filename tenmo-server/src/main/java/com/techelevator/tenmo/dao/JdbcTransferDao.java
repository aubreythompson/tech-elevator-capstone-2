package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.exceptions.TransferNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;
    private final AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, UserDao userDao, AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT * FROM tenmo_transfer WHERE transfer_id = ?;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);
        if (rowSet.next()) {
            transfer= mapRowToTransfer(rowSet);
        } else {
            throw new TransferNotFoundException("Transfer " + transferId + " not found.");
        }
        return transfer;
    }

    @Override
    public List<Transfer> getAllTransfersForUser(int userId) {
        //check that the user exists
        userDao.getUserById(userId);
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM tenmo_transfer " +
                "INNER JOIN tenmo_account " +
                "ON tenmo_account.account_id = tenmo_transfer.account_from " +
                "OR tenmo_account.account_id = tenmo_transfer.account_to " +
                "WHERE user_id = ?;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        while (rowSet.next()) {
            transfers.add(mapRowToTransfer(rowSet));
        }

        return transfers;


    }

    public List<Transfer> getTransfersForAccountByStatusId(int accountId, int statusId) {
        //check that account exists
        accountDao.getAccountByAccountId(accountId);
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM tenmo_transfer WHERE account_from = ? AND transfer_status_id = ?;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId, statusId);
        while (rowSet.next()) {
            transfers.add(mapRowToTransfer(rowSet));
        }

        return transfers;
    }

    @Override
    public Transfer create(Transfer transfer) {
        //check that both accounts exist
        accountDao.getAccountByAccountId(transfer.getAccountIdFrom());
        accountDao.getAccountByAccountId(transfer.getAccountIdTo());
        String sql = "INSERT INTO tenmo_transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "RETURNING transfer_id;";
        Transfer createdTransfer = null;
        try {
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class,
                    transfer.getTransferTypeId(),
                    transfer.getTransferStatusId(),
                    transfer.getAccountIdFrom(),
                    transfer.getAccountIdTo(),
                    transfer.getAmount());
            if (result != null) {
                createdTransfer = getTransferById(result);
            }

        } catch (DataAccessException e) {
            throw new AccountNotFoundException("From account " + transfer.getAccountIdFrom() + " or To account " + transfer.getAccountIdTo() + " not found");
        }

        return createdTransfer;
    }

    public void update(int transferId, int statusId) {
        getTransferById(transferId); //check to see if transfer exists

        String sql = "UPDATE tenmo_transfer SET transfer_status_id = ? WHERE transfer_id = ?";
        jdbcTemplate.update(sql, statusId, transferId);
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setAccountIdFrom(rowSet.getInt("account_from"));
        transfer.setAccountIdTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setUserFrom(userDao.getUserByAccountId(rowSet.getInt("account_from")));
        transfer.setUserTo(userDao.getUserByAccountId(rowSet.getInt("account_to")));
        return transfer;
    }
}
