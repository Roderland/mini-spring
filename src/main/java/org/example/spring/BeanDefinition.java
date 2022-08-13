package org.example.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanDefinition {
    private final Class<?> beanClass;
    private final List<PropertyValue> propertyValues;
    private String initMethodName;
    private String destroyMethodName;

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
        this.propertyValues = new ArrayList<>();
    }

    public BeanDefinition(Class<?> beanClass, PropertyValue... propertyValues) {
        this.beanClass = beanClass;
        this.propertyValues = Arrays.asList(propertyValues);
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public List<PropertyValue> getPropertyValues() {
        return propertyValues;
    }

    public void addPropertyValue(String name, Object value) {
        propertyValues.add(new PropertyValue(name, value));
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }
}
