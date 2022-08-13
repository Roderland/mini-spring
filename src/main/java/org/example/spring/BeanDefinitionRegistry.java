package org.example.spring;

public interface BeanDefinitionRegistry {
    void register(String beanName, BeanDefinition beanDefinition);

    BeanDefinition getBeanDefinition(String beanName);
}
