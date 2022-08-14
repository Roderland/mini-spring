package org.example.spring.test;

import org.example.spring.ApplicationContext;
import org.example.spring.test.entity.User;
import org.example.spring.test.event.UserEvent;
import org.example.spring.test.service.UserService;
import org.junit.Test;

public class TestApi {

    @Test
    public void main() {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.loadBeanDefinitions("classpath:spring.xml");

        User user = applicationContext.getBean("userFactory", User.class);
        UserService userService = applicationContext.getBean("userService", UserService.class, user.getUid());
        System.out.println(userService.queryUserInfo());

        System.out.println(userService.getApplicationContext());
        applicationContext.publishEvent(new UserEvent(this, user));
    }
}
