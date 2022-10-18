package org.example.eventbus;

import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;

/**
 * @author huang
 */
@Component
public class ByeEventBusListenerImpl implements MyEventBusListener {

    @Subscribe
    public void listen(ByeEvent byeEvent) {
        System.out.println(String.format("bye %s", byeEvent.getMessage()));
    }
}
