package org.example.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class HelloServiceInvocationHandler implements InvocationHandler {
    private Object object;

    public Object getProxyInstance(Object object) {
        this.object = object;
        return Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("sayHello".equals(method.getName())) {
            return proxySayHello(method, args);
        }
        if ("anotherMethod".equals(method.getName())) {
            return proxyAnotherMethod(method, args);
        }
        return method.invoke(object, args);
    }
    private Object proxySayHello(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        String result = (String) method.invoke(object, args);
        return  "2022-09-30 " + result;
    }
    private Object proxyAnotherMethod(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        String result = (String) method.invoke(object, args);
        return  "2022-10-01 " + result;
    }
}
