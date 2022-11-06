package org.example.design;

interface Strategy {
    void execute(String data);
}

public class Context {
    private Strategy strategy;

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void doSomething(String data) {
        strategy.execute(data);
    }
}

class ConcreteStrategyA implements Strategy {

    @Override
    public void execute(String data) {
        System.out.println("ConcreteStrategyA " + data);
    }
}

class ConcreteStrategyB implements Strategy {

    @Override
    public void execute(String data) {
        System.out.println("ConcreteStrategyB " + data);
    }
}