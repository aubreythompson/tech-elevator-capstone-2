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

    @RequestMapping(path = "/account", method = RequestMethod.POST)
    public Account getAccount(@RequestBody Long userId) {
        Account account = accountDao.getAccountByUserId(userId);
        System.out.println("test");

        return account;
    }


}
