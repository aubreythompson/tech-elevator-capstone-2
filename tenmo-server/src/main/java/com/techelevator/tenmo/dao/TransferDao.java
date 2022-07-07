package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    public Transfer getTransferById(int transferId);

    public List<Transfer> getAllTransfersForUser(int userId);

}
