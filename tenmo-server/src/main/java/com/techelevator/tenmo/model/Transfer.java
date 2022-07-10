package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

/***
 *Tranfers have:
 *
 * ID
 * Type ID (request or send)
 * Status ID (pending, approved or rejected. all sends are automatically approved. requests are approved or
 * rejected by the account requested from.
 *
 * Note that accounts and users have a one-to-one relationship.
 * The UserFrom and UserTo objects are included so that the client can easily access the usernames
 * when displaying a user's transfers.
 *
 */
public class Transfer {

    static public class transferTypes {
        public static final int REQUEST = 1;
        public static final int SEND = 2;
    }

    static public class transferStatuses {
        public static final int PENDING = 1;
        public static final int APPROVED = 2;
        public static final int REJECTED = 3;
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


    /***
     * gets type string for UI
     * @return
     */
    public String getTypeString() {
        if (transferTypeId == 1) {
            return "Request";
        } else {
            return "Send";
        }
    }

    /***
     * gets status string for UI
     * @return
     */
    public String getStatusString() {
        if (transferStatusId == 1) {
            return "Pending";
        } else if (transferStatusId == 2) {
            return "Approved";
        } else {
            return "Rejected";
        }
    }

    /***
     * establishes equality for testing
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transfer)) return false;
        Transfer transfer = (Transfer) o;
        return transferId == transfer.transferId && transferTypeId == transfer.transferTypeId &&
                transferStatusId == transfer.transferStatusId && accountIdFrom == transfer.accountIdFrom &&
                accountIdTo == transfer.accountIdTo && amount.doubleValue() == transfer.amount.doubleValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferId, transferTypeId, transferStatusId, accountIdFrom, accountIdTo, amount.doubleValue());
    }
}
