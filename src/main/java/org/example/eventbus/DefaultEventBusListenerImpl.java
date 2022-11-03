package org.example.eventbus;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;

/**
 * @author huang
 */
@Component
public class DefaultEventBusListenerImpl implements MyEventBusListener {

    @Subscribe
    public void listen(DeadEvent deadEvent) {
        throw new RuntimeException(deadEvent.getEvent().toString());
    }
}
