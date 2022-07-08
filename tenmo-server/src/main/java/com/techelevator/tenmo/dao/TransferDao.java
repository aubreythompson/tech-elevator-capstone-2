package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    public Transfer getTransferById(int transferId);

    public List<Transfer> getAllTransfersForUser(int userId);

    public List<Transfer> getTransfersForAccountByStatusId(int accountId, int statusId);

    public void update(int transferId, int statusId);

    public Transfer create(Transfer transfer);

}
