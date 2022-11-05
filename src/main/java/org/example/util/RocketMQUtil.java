package org.example.util;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

public class RocketMQUtil {
    public static DefaultMQProducer getDefaultMQProducer() {
        DefaultMQProducer producer = new DefaultMQProducer("defaultMQProducer");
        producer.setNamesrvAddr("localhost:9876");
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        return producer;
    }
}
