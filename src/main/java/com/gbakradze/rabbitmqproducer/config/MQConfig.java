package com.gbakradze.rabbitmqproducer.config;


import com.rabbitmq.client.AMQP;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    public static String QUEUE = "some_new_queue";
    public static String EXCHANGE = "message_exchange";
    public static String ROUTING_KEY = "ana.orange";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }




    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }


    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout_exchange");
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("ana.orange");
    }


    @Bean
    public Binding binding2(Queue queue, FanoutExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange);
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory("localhost");
        factory.setUsername("admin");
        factory.setPassword("admin");
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

}
