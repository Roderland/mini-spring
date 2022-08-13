package org.example.spring;

public interface BeanFactory {
    <T> T createBean(String beanName, Class<T> beanType, Object... args);
    <T> T getBean(String beanName, Class<T> beanType, Object... args);
}
