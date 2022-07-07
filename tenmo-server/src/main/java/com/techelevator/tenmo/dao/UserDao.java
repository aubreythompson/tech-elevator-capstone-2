package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    List<User> getOtherUsers(int userId);

    User getUserById(int id);

    User getUserByAccountId(int accountId);

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);
}
