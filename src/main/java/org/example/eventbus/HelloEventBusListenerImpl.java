package org.example.eventbus;

import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;

/**
 * @author huang
 */
@Component
public class HelloEventBusListenerImpl implements MyEventBusListener {

    @Subscribe
    public void listen(HelloEvent helloEvent) {
        System.out.println(String.format("hello %s", helloEvent.getMessage()));
    }
}
