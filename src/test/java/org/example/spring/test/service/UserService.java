package org.example.spring.test.service;

import org.example.spring.test.dao.UserDao;

public class UserService {
    private String uid;
    private UserDao userDao;

    public UserService(String uid) {
        this.uid = uid;
    }

    public String queryUserInfo() {
        return userDao.queryUserName(uid);
    }
}
