package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exceptions.AccountNotFoundException;
import com.techelevator.tenmo.exceptions.InsufficientFundsException;
import com.techelevator.tenmo.exceptions.InvalidUserException;
import com.techelevator.tenmo.exceptions.NonPositiveTransferException;
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

    /***
     * GET method for all transfers for the logged in user
     * @param principal
     * @return
     */
    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public Transfer[] getTransfersForUser(Principal principal) {
        String userName = principal.getName();
        int userId = userDao.findIdByUsername(userName);

        return transferDao.getAllTransfersForUser(userId).toArray(new Transfer[0]);

    }

    /***
     * POST method for creating send tranfer
     * @param principal
     * @param transferDto includes accounts ID to and from and the amount
     * @throws Exception - invalid user exception if the user logged in is wrong
     */

    @Transactional
    @RequestMapping(path = "/send-bucks", method = RequestMethod.POST)
    public void sendBucks(Principal principal, @Valid @RequestBody TransferDTO transferDto) throws Exception {

        String userName = principal.getName();
        User currentUser = userDao.findByUsername(userName);

        //check that the user logged in is the user trying to send money
        if (currentUser.getId()!=transferDto.getUserIdFrom()) {
            throw new InvalidUserException();
        }

        Account accountFrom = accountDao.getAccountByUserId(currentUser.getId());
        Account accountTo = accountDao.getAccountByUserId(transferDto.getUserIdTo());

        transferMoney(accountFrom,accountTo,transferDto.getAmount());
        Transfer transfer = new Transfer(0, Transfer.transferTypes.SEND,Transfer.transferStatuses.APPROVED,accountFrom.getAccountId(),accountTo.getAccountId(),transferDto.getAmount());
        transferDao.create(transfer);

    }



    /***
     * POST method for creating request transfer
     * @param principal
     * @param transferDto includes accounts ID to and from and the amount
     * @throws Exception - invalid user exception if the user logged in is wrong
     */
    @Transactional
    @RequestMapping(path = "/request-bucks", method = RequestMethod.POST)
    public void requestBucks(Principal principal, @Valid @RequestBody TransferDTO transferDto) throws Exception {

        String userName = principal.getName();
        User currentUser = userDao.findByUsername(userName);

        //check that the user logged in is the user requesting money
        if (currentUser.getId() != transferDto.getUserIdTo()) {
            throw new InvalidUserException();
        }

        Account accountTo = accountDao.getAccountByUserId(currentUser.getId());
        Account accountFrom = accountDao.getAccountByUserId(transferDto.getUserIdFrom());

        Transfer transfer = new Transfer(0,Transfer.transferTypes.REQUEST,Transfer.transferStatuses.PENDING,accountFrom.getAccountId(),accountTo.getAccountId(),transferDto.getAmount());
        transferDao.create(transfer);
    }

    /***
     * get all the pending requests FROM the user logged in
     * @param principal
     * @return
     * @throws Exception for data access exceptions
     */
    @RequestMapping(path = "/pending-requests", method = RequestMethod.GET)
    public Transfer[] getPendingRequests(Principal principal) throws Exception {

        String userName = principal.getName();
        User currentUser = userDao.findByUsername(userName);

        Account account = accountDao.getAccountByUserId(currentUser.getId());

        Transfer[] transfers = transferDao.getTransfersForAccountByStatusId(account.getAccountId(), Transfer.transferStatuses.PENDING).toArray(new Transfer[0]);
        return transfers;
    }

    /***
     * updates pending request to approve
     * @param principal
     * @param transferId
     * @throws Exception for data access exceptions, invalid users
     */
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

        transferMoney(accountFrom,accountTo,transfer.getAmount());
        transferDao.update(transferId,Transfer.transferStatuses.APPROVED);

    }

    /***
     * updates pending request to rejected
     * @param principal
     * @param transferId
     * @throws Exception
     */
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
        transferDao.update(transferId,Transfer.transferStatuses.REJECTED);
    }

    /***
     * Makes sure there is enough funds in the account sending the money,
     * then updates both accounts.
     * @param accountFrom
     * @param accountTo
     * @param amount
     * @throws InsufficientFundsException
     * @throws AccountNotFoundException
     */
    @Transactional
    private void transferMoney(Account accountFrom, Account accountTo, BigDecimal amount) throws InsufficientFundsException, AccountNotFoundException {

        BigDecimal newAmountFrom = accountFrom.getBalance().subtract(amount);
        if (newAmountFrom.compareTo(BigDecimal.ZERO)==-1) {
            throw new InsufficientFundsException();
        }
        BigDecimal newAmountTo = accountTo.getBalance().add(amount);
        accountDao.update(accountFrom.getAccountId(), newAmountFrom);
        accountDao.update(accountTo.getAccountId(), newAmountTo);

    }

}
