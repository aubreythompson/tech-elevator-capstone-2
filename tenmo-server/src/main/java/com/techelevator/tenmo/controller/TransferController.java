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

        String userName = principal.getName();
        User currentUser = userDao.findByUsername(userName);

        if (transferDto.getAmount().compareTo(BigDecimal.ZERO)!=1) {
            throw new NonPositiveTransferException();
        }

        if (currentUser.getId()!=transferDto.getUserIdFrom()) {
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
        accountDao.update(new Account(accountTo.getAccountId(), transferDto.getUserIdTo(), newAmountTo));

        Transfer transfer = new Transfer(0,2,2,accountFrom.getAccountId(),accountTo.getAccountId(),transferDto.getAmount());
        transferDao.create(transfer);

    }

    @Transactional
    @RequestMapping(path = "/request-bucks", method = RequestMethod.POST)
    //adds transfer to database with type request, status pending
    public void requestBucks(Principal principal, @Valid @RequestBody TransferDTO transferDto) throws Exception {

        String userName = principal.getName();
        User currentUser = userDao.findByUsername(userName);

        if (transferDto.getAmount().compareTo(BigDecimal.ZERO) != 1) {
            throw new NonPositiveTransferException();
        }

        if (currentUser.getId() != transferDto.getUserIdTo()) {
            throw new InvalidUserException();
        }

        Account accountTo = accountDao.getAccountByUserId(currentUser.getId());
        Account accountFrom = accountDao.getAccountByUserId(transferDto.getUserIdFrom());

        Transfer transfer = new Transfer(0,1,1,accountFrom.getAccountId(),accountTo.getAccountId(),transferDto.getAmount());
        transferDao.create(transfer);
    }


}
