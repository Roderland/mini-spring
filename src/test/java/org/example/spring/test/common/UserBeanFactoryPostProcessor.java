package org.example.spring.test.common;

import org.example.spring.*;
import org.example.spring.bean.BeanDefinition;
import org.example.spring.bean.BeansException;
import org.example.spring.bean.PropertyValue;
import org.example.spring.processor.BeanFactoryPostProcessor;

public class UserBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ApplicationContext applicationContext) throws BeansException {
        BeanDefinition beanDefinition = applicationContext.getBeanDefinition("userService");
        beanDefinition.getPropertyValues().add(new PropertyValue("company", "bytedance"));
    }
}
