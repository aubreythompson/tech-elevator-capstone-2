package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.InvalidTransferException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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


    @RequestMapping(path = "/send-bucks", method = RequestMethod.POST)
    public void sendBucks(Principal principal, @Valid @RequestBody Transfer transfer) throws InvalidTransferException {
        String userName = principal.getName();
        int currentUserId = userDao.findIdByUsername(userName);
        if (currentUserId==transfer.getAccountIdFrom()) {
            transferDao.sendBucks(transfer);
        }

    }




}
