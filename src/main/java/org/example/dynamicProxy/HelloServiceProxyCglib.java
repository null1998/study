package org.example.dynamicProxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class HelloServiceProxyCglib implements MethodInterceptor {
    private Enhancer enhancer = new Enhancer();

    public Object getProxy(Class<?> clazz) {
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return "cglib " + proxy.invokeSuper(obj, args);
    }
}
