package org.example.spring.processor;

import org.example.spring.ApplicationContext;
import org.example.spring.BeansException;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(ApplicationContext applicationContext) throws BeansException;
}
