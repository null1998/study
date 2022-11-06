package org.example.design;

public interface Mediator {
    void notify(Colleague colleague, String event);
}

class ConcreteMediator implements Mediator {

    @Override
    public void notify(Colleague colleague, String event) {
        if (colleague instanceof ConcreteColleagueA) {
            colleague.execute(event);
        }
        if (colleague instanceof ConcreteColleagueB) {
            colleague.execute(event);
        }
    }
}

abstract class Colleague {
    protected Mediator mediator;

    public Colleague(Mediator mediator) {
        this.mediator = mediator;
    }

    abstract void onEvent();

    abstract void execute(String event);
}

class ConcreteColleagueA extends Colleague {

    public ConcreteColleagueA(Mediator mediator) {
        super(mediator);
    }

    @Override
    void onEvent() {
        mediator.notify(this, "event a");
    }

    @Override
    void execute(String event) {
        System.out.println("ConcreteColleagueA execute " + event);
    }
}

class ConcreteColleagueB extends Colleague {

    public ConcreteColleagueB(Mediator mediator) {
        super(mediator);
    }

    @Override
    void onEvent() {
        mediator.notify(this, "event b");
    }

    @Override
    void execute(String event) {
        System.out.println("ConcreteColleagueB execute " + event);
    }
}
