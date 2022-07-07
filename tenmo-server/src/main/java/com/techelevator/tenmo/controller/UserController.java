package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {
    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(path = "/users/{userId}", method = RequestMethod.GET)
    public String getUsername(@PathVariable int userId) {

        User user = userDao.getUserById(userId);
        return user.getUsername();
    }

    @RequestMapping(path = "/get-other-users", method = RequestMethod.GET)
    public User[] getOtherUsers(Principal principal) {
        String userName = principal.getName();
        int userId = userDao.findIdByUsername(userName);
        return userDao.getOtherUsers(userId).toArray(new User[0]);

    }
}
