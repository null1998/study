package org.example.design;

public abstract class Factory {
    abstract Product create();
}

class ConcreteFactoryA extends Factory {

    @Override
    Product create() {
        return new ConcreteProductA();
    }
}

class ConcreteFactoryB extends Factory {

    @Override
    Product create() {
        return new ConcreteProductB();
    }
}
