package org.example.spring.test.service;

import org.example.spring.test.dao.UserDao;

public class UserService {
    private String uid;

    private String company;

    private String location;
    private UserDao userDao;

    public UserService(String uid) {
        this.uid = uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String queryUserInfo() {
        return uid + " " + company + " " + location + " " + userDao.queryUserName(uid);
    }
}
