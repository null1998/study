package org.example.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.example.util.RocketMQUtil;
import org.junit.Test;

public class RocketMQTest {
    @Test
    public void testSend() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        DefaultMQProducer producer = RocketMQUtil.getDefaultMQProducer();
        Message message = new Message("testSend", "testSend", "001", "testSendMsg".getBytes());
        producer.send(message);
        producer.send(message);
    }
}
