package org.example.spi;

public class HelloChineseServiceProviderImpl implements HelloServiceProvider {
    @Override
    public String sayHello() {
        return "你好";
    }
}
