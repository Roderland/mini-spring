package org.example.spring.test.service;

import org.example.spring.ApplicationContext;
import org.example.spring.BeansException;
import org.example.spring.aware.ApplicationContextAware;
import org.example.spring.test.dao.UserDao;

public class UserService implements ApplicationContextAware{
    private String uid;
    private String company;
    private String location;
    private UserDao userDao;
    private ApplicationContext applicationContext;

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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
