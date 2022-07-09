package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.security.SecurityUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

@RestController
public class AccountController {

    private final AccountDao accountDao;
    private final UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public Account getAccount(Principal principal) {
        String userName = principal.getName();
        int userId = userDao.findIdByUsername(userName);

        Account account = accountDao.getAccountByUserId(userId);

        return account;
    }


}
