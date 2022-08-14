package org.example.spring.processor;

import org.example.spring.ApplicationContext;
import org.example.spring.bean.BeansException;
import org.example.spring.aware.ApplicationContextAware;

public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInit(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware aware) {
            aware.setApplicationContext(applicationContext);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInit(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
