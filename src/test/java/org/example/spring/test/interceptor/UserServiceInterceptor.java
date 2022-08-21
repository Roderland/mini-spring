package org.example.spring.test.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class UserServiceInterceptor implements MethodInterceptor  {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return methodInvocation.proceed();
        } finally {
            System.out.println("====> Begin By AOP");
            System.out.println("method name: " + methodInvocation.getMethod());
            System.out.println("method time: " + (System.currentTimeMillis() - start) + "ms");
            System.out.println("====> End\r\n");
        }
    }
}
