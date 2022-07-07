package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.InvalidTransferException;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    public Transfer getTransferById(int transferId);

    public List<Transfer> getAllTransfersForUser(int userId);

    public void sendBucks(Transfer transfer) throws InvalidTransferException;

}
