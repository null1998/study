package org.example.dynamicProxy;

public class HelloChineseServiceImpl implements IHelloService {
    @Override
    public String sayHello() {
        return "你好";
    }

    @Override
    public String anotherMethod() {
        return "另一个方法";
    }
}
