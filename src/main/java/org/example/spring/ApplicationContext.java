package org.example.spring;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.example.spring.resource.ClassPathResource;
import org.example.spring.resource.Resource;
import org.example.spring.resource.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ApplicationContext implements BeanDefinitionRegistry, BeanFactory, ResourceLoader {

    private final Map<String, BeanDefinition> registry = new HashMap<>();
    private final Map<String, Object> factory = new HashMap<>();

    @Override
    public void register(String beanName, BeanDefinition beanDefinition) {
        registry.put(beanName, beanDefinition);
    }

    @Override
    public Object createBean(String beanName, Object... args) {
        BeanDefinition beanDefinition = registry.get(beanName);
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?> beanConstructor;
        Object bean;
        try {
            if (args == null || args.length == 0) {
                beanConstructor = beanClass.getConstructor();
            } else {
                beanConstructor = Arrays.stream(beanClass.getDeclaredConstructors())
                        .filter(constructor -> args.length != constructor.getTypeParameters().length)
                        .findFirst()
                        .orElseThrow(() -> new BeansException("find constructor failed."));
            }
            bean = beanConstructor.newInstance(args);
            applyPropertyValues(bean, beanDefinition);
        } catch (Throwable e) {
            throw new BeansException("createBean failed:", e);
        }
        factory.put(beanName, bean);
        return bean;
    }

    @Override
    public Object getBean(String beanName, Object... args) {
        Object bean = factory.get(beanName);
        if (bean == null) {
            return createBean(beanName, args);
        }
        return bean;
    }

    private void applyPropertyValues(Object bean, BeanDefinition beanDefinition) {
        beanDefinition.getPropertyValues().forEach(propertyValue -> {
            String name = propertyValue.name();
            Object value = propertyValue.value();
            if (value instanceof BeanReference beanReference) {
                value = getBean(beanReference.getBeanName());
            }
            BeanUtil.setFieldValue(bean, name, value);
        });
    }

    @Override
    public Resource getResource(String location) {
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        } else {
            throw new BeansException("only support classpath resource.");
        }
    }

    public void loadBeanDefinitions(String... locations) {
        for (String location : locations) {
            Resource resource = this.getResource(location);
            try (InputStream resourceInputStream = resource.getInputStream()) {
                loadBeanDefinitionFromXML(resourceInputStream);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadBeanDefinitionFromXML(InputStream inputStream) throws ClassNotFoundException {
        Document doc = XmlUtil.readXML(inputStream);
        Element root = doc.getDocumentElement();
        NodeList childNodes = root.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {
            if (!(childNodes.item(i) instanceof Element bean)) continue;
            if (!Objects.equals("bean", bean.getNodeName())) continue;

            String id = bean.getAttribute("id");
            String name = bean.getAttribute("name");
            String className = bean.getAttribute("class");

            Class<?> clazz = Class.forName(className);
            String beanName = StrUtil.isNotBlank(id) ? id : name;
            if (StrUtil.isBlank(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            if (registry.containsKey(beanName)) {
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }

            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                if (!(bean.getChildNodes().item(j) instanceof Element property)) continue;
                if (!Objects.equals("property", property.getNodeName())) continue;

                String attrName = property.getAttribute("name");
                String attrValue = property.getAttribute("value");
                String attrRef = property.getAttribute("ref");

                Object value = StrUtil.isNotBlank(attrRef) ? new BeanReference(attrRef) : attrValue;
                beanDefinition.addPropertyValue(attrName, value);
            }

            register(beanName, beanDefinition);
        }
    }
}
