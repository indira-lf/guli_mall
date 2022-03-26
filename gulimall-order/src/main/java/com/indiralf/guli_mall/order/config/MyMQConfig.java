package com.indiralf.guli_mall.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @time 2022/3/18 14:19
 * @Description- TODO
 */
@Configuration
public class MyMQConfig {

//    @RabbitListener(queues = "order.release.order.queue")
//    public void listener(Order)

    @Bean
    public Queue orderDelayQueue(){

        Map<String,Object> arguments = new HashMap<>(16);

        arguments.put("x-dead-letter-exchange","order-event-exchange");
        arguments.put("x-dead-letter-routing-key","order.release.order");
        arguments.put("x-message-tt10",60000);

        Queue queue = new Queue("order.delay.queue", true, false, false,arguments);

        return queue;
    }

    @Bean
    public Queue orderReleaseOrderQueue(){

        Queue queue = new Queue("order.release.order.queue", true, false, false);

        return queue;
    }

    @Bean
    public Exchange orderEventExchange(){

        TopicExchange topicExchange = new TopicExchange("order-event-exchange", true, false);
        return  topicExchange;
    }

    @Bean
    public Binding orderCreateOrderBinding(){
        return new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create.order",
                null);
    }

    @Bean
    public Binding orderReleaseOrderBinding(){
        return new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create.order",
                null);
    }

}
