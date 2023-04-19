package org.example.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@SpringBootTest
public class RocketMQTest {
    @Resource
    private DefaultMQProducer defaultMQProducer;

    @Test
    public void testSync() {
        Message message = new Message("topic-01", "topic-01-tag-a", String.format("message-body-%s", LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()).getBytes(StandardCharsets.UTF_8));
        try {
            defaultMQProducer.send(message);
            defaultMQProducer.shutdown();
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            throw new RuntimeException("rocketMq生产者发送消息失败", e);
        }
    }
}
