package org.example.spring.test;

import org.example.spring.ApplicationContext;
import org.example.spring.aop.*;
import org.example.spring.test.entity.User;
import org.example.spring.test.event.UserEvent;
import org.example.spring.test.interceptor.UserServiceInterceptor;
import org.example.spring.test.service.IUserService;
import org.junit.Test;

public class TestApi {

    @Test
    public void main() {
        ApplicationContext applicationContext = new ApplicationContext();
        applicationContext.loadBeanDefinitions("classpath:spring.xml");

        User user = applicationContext.getBean("userFactory", User.class);
        IUserService userService = applicationContext.getBean("userService", IUserService.class, user.getUid());
        System.out.println(userService.queryUserInfo());

        System.out.println(userService.getApplicationContext());
        applicationContext.publishEvent(new UserEvent(this, user));

        // 组装代理信息
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTargetSource(new TargetSource(userService));
        advisedSupport.setMethodInterceptor(new UserServiceInterceptor());
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut(
                "execution(* org.example.spring.test.service.IUserService.*(..))"));

        // 代理对象(JdkDynamicAopProxy)
        IUserService proxy_jdk = (IUserService) new JdkDynamicAopProxy(advisedSupport).getProxy();
        // 测试调用
        System.out.println("test result: " + proxy_jdk.queryUserInfo());
        System.out.println("test result: " + proxy_jdk.register("19999", "roderland"));

    }
}
