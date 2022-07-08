package com.techelevator.tenmo.model;

import java.math.BigDecimal;

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



}
