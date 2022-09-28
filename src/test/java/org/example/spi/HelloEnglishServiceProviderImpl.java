package org.example.spi;

public class HelloEnglishServiceProviderImpl implements HelloServiceProvider {
    @Override
    public String sayHello() {
        return "hello";
    }
}
