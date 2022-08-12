package org.example.spring;

public interface BeanFactory {
    Object createBean(String beanName, Object... args);
    Object getBean(String beanName, Object... args);
}
