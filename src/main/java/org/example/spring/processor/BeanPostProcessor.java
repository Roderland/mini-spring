package org.example.spring.processor;

import org.example.spring.BeansException;

public interface BeanPostProcessor {
    Object postProcessBeforeInit(Object bean, String beanName) throws BeansException;
    Object postProcessAfterInit(Object bean, String beanName) throws BeansException;
}
