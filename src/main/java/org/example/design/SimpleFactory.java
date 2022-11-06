package org.example.design;

interface Product {

}

public class SimpleFactory {
    public Product create(String type) {
        if ("A".equals(type)) {
            return new ConcreteProductA();
        }
        if ("B".equals(type)) {
            return new ConcreteProductB();
        }
        throw new RuntimeException("无法创建实例，错误的类型：" + type);
    }
}

class ConcreteProductA implements Product {

}

class ConcreteProductB implements Product {

}
