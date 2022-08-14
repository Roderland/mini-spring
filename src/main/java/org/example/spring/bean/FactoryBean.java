package org.example.spring.bean;

public interface FactoryBean<T> {
    T getObject();
    Class<T> getObjectType();
}
