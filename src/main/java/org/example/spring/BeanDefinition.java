package org.example.spring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BeanDefinition {
    private final Class<?> beanClass;
    private final List<PropertyValue> propertyValues;

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
}
