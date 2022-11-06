package org.example.design;

import java.util.ArrayList;
import java.util.List;

interface Subscriber {
    void execute(String data);
}

public class Publisher {
    private List<Subscriber> subscribers;

    public Publisher() {
        this.subscribers = new ArrayList<>();
    }

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void notify(String data) {
        for (Subscriber subscriber : subscribers) {
            subscriber.execute(data);
        }
    }
}

class NormalSubscriber implements Subscriber {

    @Override
    public void execute(String data) {
        System.out.println("NormalSubscriber " + data);
    }
}

class VIPSubscriber implements Subscriber {

    @Override
    public void execute(String data) {
        System.out.println("VIPSubscriber " + data);
    }
}