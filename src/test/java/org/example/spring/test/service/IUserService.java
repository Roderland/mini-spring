package org.example.spring.test.service;

import org.example.spring.aware.ApplicationContextAware;

public interface IUserService extends ApplicationContextAware {
    String queryUserInfo();

    String register(String uid, String username);
}
