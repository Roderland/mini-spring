package org.example.spring;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import org.example.spring.processor.ApplicationContextAwareProcessor;
import org.example.spring.processor.BeanFactoryPostProcessor;
import org.example.spring.processor.BeanPostProcessor;
import org.example.spring.resource.ClassPathResource;
import org.example.spring.resource.Resource;
import org.example.spring.resource.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class ApplicationContext implements BeanDefinitionRegistry, BeanFactory, ResourceLoader {

    private final Map<String, BeanDefinition> registry = new HashMap<>();
    private final Map<String, Object> factory = new HashMap<>();
    private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public ApplicationContext() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
        beanPostProcessorList.add(new ApplicationContextAwareProcessor(this));
    }

    @Override
    public void register(String beanName, BeanDefinition beanDefinition) {
        registry.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return registry.get(beanName);
    }

    @Override
    public <T> T createBean(String beanName, Class<T> beanType, Object... args) {
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

            // before init Bean
            bean = applyBeanPostProcessorBeforeInit(bean, beanName);
            // init Bean method
            invokeInitMethod(bean, beanDefinition);
            // after init Bean
            bean = applyBeanPostProcessorAfterInit(bean, beanName);
        } catch (Throwable e) {
            throw new BeansException("createBean failed:", e);
        }
        factory.put(beanName, bean);
        return (T) bean;
    }

    private void invokeInitMethod(Object bean, BeanDefinition beanDefinition) throws Exception {
        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isNotBlank(initMethodName)) {
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            initMethod.invoke(bean);
        }
    }

    private Object applyBeanPostProcessorBeforeInit(Object bean, String beanName) {
        Object result = bean;
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            result = beanPostProcessor.postProcessBeforeInit(result, beanName);
        }
        return result;
    }

    private Object applyBeanPostProcessorAfterInit(Object bean, String beanName) {
        Object result = bean;
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
            result = beanPostProcessor.postProcessAfterInit(result, beanName);
        }
        return result;
    }

    @Override
    public <T> T getBean(String beanName, Class<T> beanType, Object... args) {
        Object bean = factory.get(beanName);
        if (bean == null) {
            return createBean(beanName, beanType, args);
        }
        return (T) bean;
    }

    private void applyPropertyValues(Object bean, BeanDefinition beanDefinition) {
        beanDefinition.getPropertyValues().forEach(propertyValue -> {
            String name = propertyValue.name();
            Object value = propertyValue.value();
            if (value instanceof BeanReference beanReference) {
                value = getBean(beanReference.getBeanName(), bean.getClass());
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
        // 读取配置文件并加载 BeanDefinition
        for (String location : locations) {
            Resource resource = this.getResource(location);
            try (InputStream resourceInputStream = resource.getInputStream()) {
                loadBeanDefinitionFromXML(resourceInputStream);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        // 实例化 BeanFactoryPostProcessor 并执行来修改 BeanDefinition
        invokeBeanFactoryPostProcessor();
        // 注册 BeanPostProcessor 确保 Bean 实例化前后可以用来修改 Bean
        registerBeanPostProcessor();
    }

    private void registerBeanPostProcessor() {
        for (Map.Entry<String, BeanDefinition> entry : registry.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (BeanPostProcessor.class.isAssignableFrom(beanDefinition.getBeanClass())) {
                beanPostProcessorList.add(createBean(beanName, BeanPostProcessor.class));
            }
        }
    }

    private void invokeBeanFactoryPostProcessor() {
        for (Map.Entry<String, BeanDefinition> entry : registry.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (BeanFactoryPostProcessor.class.isAssignableFrom(beanDefinition.getBeanClass())) {
                BeanFactoryPostProcessor beanFactoryPostProcessor = createBean(beanName, BeanFactoryPostProcessor.class);
                beanFactoryPostProcessor.postProcessBeanFactory(this);
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
            String initMethod = bean.getAttribute("init-method");
            String destroyMethod = bean.getAttribute("destroy-method");

            Class<?> clazz = Class.forName(className);
            String beanName = StrUtil.isNotBlank(id) ? id : name;
            if (StrUtil.isBlank(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            if (registry.containsKey(beanName)) {
                throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
            }

            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            beanDefinition.setInitMethodName(initMethod);
            beanDefinition.setDestroyMethodName(destroyMethod);

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

    private void close() {
        for (Map.Entry<String, BeanDefinition> entry : registry.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            Class<?> beanClass = beanDefinition.getBeanClass();
            String destroyMethodName = beanDefinition.getDestroyMethodName();

            if (StrUtil.isNotBlank(destroyMethodName)) {
                Object bean = factory.get(beanName);
                if (bean != null) {
                    Method destroyMethod = null;
                    try {
                        destroyMethod = beanClass.getMethod(destroyMethodName);
                        destroyMethod.invoke(bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
