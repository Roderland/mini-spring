package org.example.spring.bean;

public interface BeanDefinitionRegistry {
    void register(String beanName, BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(String beanName);
}
