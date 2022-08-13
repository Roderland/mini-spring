package org.example.spring.test;

import org.example.spring.ApplicationContext;
import org.example.spring.test.service.UserService;
import org.junit.Test;

public class TestApi {

    @Test
    public void main() {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.loadBeanDefinitions("classpath:spring.xml");

        UserService userService = applicationContext.getBean("userService", UserService.class, "10001");
        System.out.println(userService.queryUserInfo());
    }
}
