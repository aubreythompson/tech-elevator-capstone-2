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

    /***
     * Note: accounts and users are one to one, so there is one account to get for the
     * authenticated user accessed by principal
     * @param principal
     * @return
     * @throws Exception
     */

    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public Account getAccount(Principal principal) throws Exception {
        String userName = principal.getName();
        int userId = userDao.findIdByUsername(userName);

        Account account = accountDao.getAccountByUserId(userId);

        return account;
    }


}
