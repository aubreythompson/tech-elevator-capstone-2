package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

@RestController
public class TransferController {

    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final UserDao userDao;

    public TransferController(AccountDao accountDao, TransferDao transferDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public Transfer[] getTransfersForUser(Principal principal) {
        String userName = principal.getName();
        int userId = userDao.findIdByUsername(userName);

        return transferDao.getAllTransfersForUser(userId).toArray(new Transfer[0]);

    }


    @Transactional
    @RequestMapping(path = "/send-bucks", method = RequestMethod.POST)
    public void sendBucks(Principal principal, @Valid @RequestBody TransferDTO transferDto) throws Exception {
        /**I should be able to choose from a list of users to send TE Bucks to.
         I must not be allowed to send money to myself.
         A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
         The receiver's account balance is increased by the amount of the transfer.
         The sender's account balance is decreased by the amount of the transfer.
         I can't send more TE Bucks than I have in my account.
         I can't send a zero or negative amount.
         A Sending Transfer has an initial status of Approved.
         */
        String userName = principal.getName();
        User currentUser = userDao.findByUsername(userName);

        if (transferDto.getAmount().compareTo(BigDecimal.ZERO)!=1) {
            throw new NonPositiveTransferException();
        }

        if (currentUser.getId()!=transferDto.getUserIdFrom() || currentUser.getId()==transferDto.getUserIdTo()) {
            throw new InvalidUserException();
        }
        Account accountFrom = accountDao.getAccountByUserId(currentUser.getId());
        BigDecimal accountFromBalance = accountFrom.getBalance();
        if (accountFromBalance.compareTo(transferDto.getAmount())==-1) {
            throw new InsufficientFundsException();
        }
        BigDecimal newAmountFrom = accountFromBalance.subtract(transferDto.getAmount());
        Account accountTo = accountDao.getAccountByUserId(transferDto.getUserIdTo());
        BigDecimal accountToBalance = accountTo.getBalance();
        BigDecimal newAmountTo = accountToBalance.add(transferDto.getAmount());
        accountDao.update(new Account(accountFrom.getAccountId(), currentUser.getId(), newAmountFrom));
        accountDao.update(new Account(accountTo.getAccountId(), currentUser.getId(), newAmountTo));

        Transfer transfer = new Transfer(0,1,1,transferDto.getUserIdFrom(),transferDto.getUserIdTo(),transferDto.getAmount());
        transferDao.create(transfer);

    }




}
