package com.gbakradze.rabbitmqproducer.api;

import com.gbakradze.rabbitmqproducer.CustomMessage;
import com.gbakradze.rabbitmqproducer.config.MQConfig;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
public class MessagePublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @PostMapping("/publish")
    public String publishMessage(@RequestBody CustomMessage message) {
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageDate(new Date());
        rabbitTemplate.convertAndSend(MQConfig.EXCHANGE,
                MQConfig.ROUTING_KEY,
                message);

        return "Message Published";
    }

    @PostMapping("/fanoutpublish")
    public String publishUsingFanout(@RequestBody CustomMessage message) {
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageDate(new Date());
        rabbitTemplate.setExchange("fanout_exchange");
        rabbitTemplate.convertAndSend(MQConfig.ROUTING_KEY,
                message);

        return "Sent Using Fanout";
    }


    @PostMapping("/removeQueue")
    public String removeQueue(@RequestBody String queueName) {
        rabbitAdmin.deleteQueue(queueName);

        return "Queue: " + queueName + " deleted successfully";
    }
}
