package org.example.eventbus;

import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author huang
 */
@SpringBootTest
public class EventBusTest {
    @Resource
    private EventBus eventBus;

    @Test
    public void testGuavaEventBus() {
        HelloEvent helloEvent = new HelloEvent();
        helloEvent.setMessage("helloEvent");
        eventBus.post(helloEvent);
        ByeEvent byeEvent = new ByeEvent();
        byeEvent.setMessage("byeEvent");
        eventBus.post(byeEvent);
        eventBus.post("deadEvent");
        eventBus.post(123);
    }
}
