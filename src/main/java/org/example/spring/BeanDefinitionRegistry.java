package org.example.spring;

public interface BeanDefinitionRegistry {
    void register(String beanName, BeanDefinition beanDefinition);
}
