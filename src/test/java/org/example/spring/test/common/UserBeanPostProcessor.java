package org.example.spring.test.common;

import org.example.spring.bean.BeansException;
import org.example.spring.processor.BeanPostProcessor;
import org.example.spring.test.dao.UserDao;
import org.example.spring.test.service.UserService;

public class UserBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInit(Object bean, String beanName) throws BeansException {
        if (bean instanceof UserService userService) {
            userService.setLocation("Beijing");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInit(Object bean, String beanName) throws BeansException {
        if (bean instanceof UserDao userDao) {
            userDao.putUserInfo("10002", "roderland");
        }
        return bean;
    }
}
