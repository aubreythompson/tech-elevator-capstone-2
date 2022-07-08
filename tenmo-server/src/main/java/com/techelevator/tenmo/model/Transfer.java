package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    static public class transferTypes {
        public static final int REQUEST = 1;
        public static final int SEND = 2;
    }

    private int transferId;
    private int transferTypeId;
    private int transferStatusId;
    private int accountIdFrom;
    private int accountIdTo;

    private BigDecimal amount;

    private User UserFrom;
    private User UserTo;

    public Transfer() {}

    public Transfer(int transferId, int transferTypeId, int transferStatusId, int accountIdFrom, int accountIdTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountIdFrom = accountIdFrom;
        this.accountIdTo = accountIdTo;
        this.amount = amount;
    }


    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountIdFrom() {
        return accountIdFrom;
    }

    public void setAccountIdFrom(int accountIdFrom) {
        this.accountIdFrom = accountIdFrom;
    }

    public int getAccountIdTo() {
        return accountIdTo;
    }

    public void setAccountIdTo(int accountIdTo) {
        this.accountIdTo = accountIdTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public User getUserFrom() {
        return UserFrom;
    }

    public void setUserFrom(User userFrom) {
        UserFrom = userFrom;
    }

    public User getUserTo() {
        return UserTo;
    }

    public void setUserTo(User userTo) {
        UserTo = userTo;
    }


    public String getTypeString() {
        if (transferTypeId == 1) {
            return "Request";
        } else {
            return "Send";
        }
    }

    public String getStatusString() {
        if (transferStatusId == 1) {
            return "Pending";
        } else if (transferStatusId == 2) {
            return "Approved";
        } else {
            return "Rejected";
        }
    }


}
