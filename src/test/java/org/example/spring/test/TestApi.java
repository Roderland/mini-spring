package org.example.spring.test;

import org.example.spring.ApplicationContext;
import org.example.spring.BeanDefinition;
import org.example.spring.BeanReference;
import org.example.spring.PropertyValue;
import org.example.spring.test.dao.UserDao;
import org.example.spring.test.service.UserService;
import org.junit.Test;

public class TestApi {

    @Test
    public void main() {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.loadBeanDefinitions("classpath:spring.xml");

        UserService userService = (UserService) applicationContext.getBean("userService", "10001");
        System.out.println(userService.queryUserInfo());
    }
}
