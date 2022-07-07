package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;
    private final AccountDao accountDao;
    private final UserDao userDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, AccountDao accountDao, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @Override
    public Transfer getTransferById(int transferId) {
        return null;
    }

    @Override
    public List<Transfer> getAllTransfersForUser(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM tenmo_transfer " +
                "INNER JOIN tenmo_account " +
                "ON tenmo_account.account_id = tenmo_transfer.account_from " +
                "OR tenmo_account.account_id = tenmo_transfer.account_to " +
                "WHERE user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            while (rowSet.next()) {
                transfers.add(mapRowToTransfer(rowSet));
            }

        } catch (DataAccessException e) {
        }
        return transfers;


    }

    @Override
    public void makeTransfer(Transfer transfer) {
        /**I should be able to choose from a list of users to send TE Bucks to.
         I must not be allowed to send money to myself.
         A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
         The receiver's account balance is increased by the amount of the transfer.
         The sender's account balance is decreased by the amount of the transfer.
         I can't send more TE Bucks than I have in my account.
         I can't send a zero or negative amount.
         A Sending Transfer has an initial status of Approved.
         */
        //what kind of transfer is it?
        if (transfer.getTransferTypeId() == 2) { //SEND
            //does the sending account have enough money?
            int accountIdFrom = transfer.getAccountIdFrom();

        }

        //get amount in
      //  String sql =

    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setAccountIdFrom(rowSet.getInt("account_from"));
        transfer.setAccountIdTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setUserNameFrom(userDao.getUsernameByAccountId(rowSet.getInt("account_from")));
        transfer.setUserNameTo(userDao.getUsernameByAccountId(rowSet.getInt("account_to")));
        return transfer;
    }
}
