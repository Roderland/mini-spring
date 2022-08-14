package org.example.spring.test.factory;

import org.example.spring.bean.FactoryBean;
import org.example.spring.test.entity.User;

public class UserFactory implements FactoryBean<User> {
    @Override
    public User getObject() {
        User user = new User();
        if (System.currentTimeMillis() % 2 == 0) {
            user.setUid("10002");
        } else {
            user.setUid("10001");
        }
        return user;
    }

    @Override
    public Class<User> getObjectType() {
        return User.class;
    }
}
