package org.example.design;

public abstract class Template {
    public void doSomething() {
        stepA();
        stepB();
        stepC();
    }

    abstract void stepA();

    public void stepB() {
        System.out.println("template step b");
    }

    abstract void stepC();
}

class ConcreteTemplateA extends Template {

    @Override
    void stepA() {
        System.out.println("ConcreteTemplateA " + "step a");
    }

    @Override
    void stepC() {
        System.out.println("ConcreteTemplateA " + "step c");
    }
}

class ConcreteTemplateB extends Template {

    @Override
    void stepA() {
        System.out.println("ConcreteTemplateB " + "step a");
    }

    @Override
    void stepC() {
        System.out.println("ConcreteTemplateB " + "step c");
    }
}
