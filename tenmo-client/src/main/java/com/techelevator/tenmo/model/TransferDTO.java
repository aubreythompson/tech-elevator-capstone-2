package com.techelevator.tenmo.model;

import java.math.BigDecimal;

/***
 * The Transfer Data Transfer Object is used to send only pertinent information to the server.
 *
 * We choose to send user IDs instead of account IDs because they are one-to-one.
 */

public class TransferDTO {

    private int userIdFrom;
    private int userIdTo;
    private BigDecimal amount;

    public TransferDTO(int userIdFrom, int userIdTo, BigDecimal amount) {
        this.userIdFrom = userIdFrom;
        this.userIdTo = userIdTo;
        this.amount = amount;
    }

    public TransferDTO() {}

    public int getUserIdFrom() {
        return userIdFrom;
    }

    public void setUserIdFrom(int userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    public int getUserIdTo() {
        return userIdTo;
    }

    public void setUserIdTo(int userIdTo) {
        this.userIdTo = userIdTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
