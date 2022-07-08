package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

        if (currentUser.getId()!=transferDto.getUserIdFrom()) {
            throw new InvalidUserException();
        }

        if (transferDto.getAmount().compareTo(BigDecimal.ZERO)!=1) {
            throw new NonPositiveTransferException();
        }

        Account accountFrom = accountDao.getAccountByUserId(currentUser.getId());
        Account accountTo = accountDao.getAccountByUserId(transferDto.getUserIdTo());
        BigDecimal accountFromBalance = accountFrom.getBalance();
        if (accountFromBalance.compareTo(transferDto.getAmount())==-1) {
            throw new InsufficientFundsException();
        }

        transferMoney(accountFrom,accountTo,transferDto.getAmount());
        Transfer transfer = new Transfer(0,2,2,accountFrom.getAccountId(),accountTo.getAccountId(),transferDto.getAmount()); //type = SEND, status = APPROVED
        transferDao.create(transfer);

    }

    @Transactional
    @RequestMapping(path = "/request-bucks", method = RequestMethod.POST)
    public void requestBucks(Principal principal, @Valid @RequestBody TransferDTO transferDto) throws Exception {

        String userName = principal.getName();
        User currentUser = userDao.findByUsername(userName);

        if (currentUser.getId() != transferDto.getUserIdTo()) {
            throw new InvalidUserException();
        }

        if (transferDto.getAmount().compareTo(BigDecimal.ZERO) != 1) {
            throw new NonPositiveTransferException();
        }



        Account accountTo = accountDao.getAccountByUserId(currentUser.getId());
        Account accountFrom = accountDao.getAccountByUserId(transferDto.getUserIdFrom());

        Transfer transfer = new Transfer(0,1,1,accountFrom.getAccountId(),accountTo.getAccountId(),transferDto.getAmount()); //type = REQUEST, status = PENDING
        transferDao.create(transfer);
    }

    @RequestMapping(path = "/pending-requests", method = RequestMethod.GET)
    public Transfer[] getPendingRequests(Principal principal) throws Exception {

        String userName = principal.getName();
        User currentUser = userDao.findByUsername(userName);

        Account account = accountDao.getAccountByUserId(currentUser.getId());

        Transfer[] transfers = transferDao.getTransfersForAccountByStatusId(account.getAccountId(), 1).toArray(new Transfer[0]); //PENDING
        return transfers;
    }

    @Transactional
    @RequestMapping(path = "/approve-request/{transferId}", method = RequestMethod.PUT)
    public void approvePendingRequest(Principal principal, @PathVariable int transferId) throws Exception {
        String userName = principal.getName();
        User currentUser = userDao.findByUsername(userName);
        Account accountFrom = accountDao.getAccountByUserId(currentUser.getId());
        Transfer transfer = transferDao.getTransferById(transferId);
        if (accountFrom.getAccountId()!=transfer.getAccountIdFrom()) {
            throw new InvalidUserException();
        }

        Account accountTo = accountDao.getAccountByAccountId(transfer.getAccountIdTo());

        transferDao.update(transferId,2); //APPROVED
        transferMoney(accountFrom,accountTo,transfer.getAmount());
    }

    @Transactional
    @RequestMapping(path = "/reject-request/{transferId}", method = RequestMethod.PUT)
    public void rejectPendingRequest(Principal principal, @PathVariable int transferId) throws Exception {

        String userName = principal.getName();
        User currentUser = userDao.findByUsername(userName);

        Account account = accountDao.getAccountByUserId(currentUser.getId());
        Transfer transfer = transferDao.getTransferById(transferId);
        if (account.getAccountId()!=transfer.getAccountIdFrom()) {
            throw new InvalidUserException();
        }
        transferDao.update(transferId,3); //REJECTED
    }

    @Transactional
    private void transferMoney(Account accountFrom, Account accountTo, BigDecimal amount) {
        BigDecimal newAmountFrom = accountFrom.getBalance().subtract(amount);
        BigDecimal newAmountTo = accountTo.getBalance().add(amount);
        accountDao.update(accountFrom.getAccountId(), newAmountFrom);
        accountDao.update(accountTo.getAccountId(), newAmountTo);

    }

}
