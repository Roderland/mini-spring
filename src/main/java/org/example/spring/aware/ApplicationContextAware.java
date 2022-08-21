package org.example.spring.aware;

import org.example.spring.ApplicationContext;
import org.example.spring.bean.BeansException;

public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;

    ApplicationContext getApplicationContext();
}
