package org.example.dynamicProxy;

import org.junit.Test;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.io.IOException;

public class DynamicProxyTest {
    @Test
    public void testInvocationHandler() throws IOException {
        HelloChineseServiceImpl helloChineseService = new HelloChineseServiceImpl();
        HelloServiceInvocationHandler handler = new HelloServiceInvocationHandler();
        IHelloService proxy = (IHelloService) handler.getProxyInstance(helloChineseService);
        assert "2022-09-30 你好".equals(proxy.sayHello());
        assert "2022-10-01 另一个方法".equals(proxy.anotherMethod());
        byte[] bytes = ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{helloChineseService.getClass()});
        FileOutputStream fileOutputStream = new FileOutputStream("Proxy0.class");
        fileOutputStream.write(bytes);
        fileOutputStream.close();
    }

    @Test
    public void testCglib() {
        HelloServiceProxyCglib proxyCglib = new HelloServiceProxyCglib();
        HelloChineseServiceImpl proxy = (HelloChineseServiceImpl) proxyCglib.getProxy(HelloChineseServiceImpl.class);
        assert "cglib 你好".equals(proxy.sayHello());
        assert "cglib 另一个方法".equals(proxy.anotherMethod());
    }
}
