package com.indiralf.guli_mall.order.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author
 * @time 2022/3/15 15:27
 * @Description- TODO
 */
@Configuration
public class MyRabbitConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 定制RabbitTemplate
     *  1、服务收到消息就回调
     *      1)spring.rabbitmq.publisher-confirms=true
     *      2)设置确认回调
     *  2、消息正确抵达队列进行回调
     *      1)  spring.rabbitmq.publisher-returns=true
     *          spring.rabbitmq.template.mandatory=true
     *      2)设置确认回调
     *  3、消费端确认(保证每个消息被正确消费，此时broker才可以删除这个消息)
     *      1、默认是自动确认的，只要消息接收到，服务端就会移除这个消息
     *          问题:我们收到很多消息，自动回复给服务器ack,只有一个消息处理成功，宕机了，发生消息丢失
     *              spring.rabbitmq.listener.simple.acknowledge-mode=manual
     *      2、如何签收
     *          channel.basicAck(deliveryTag,false);签收,业务成功完成就该签收
     *          channel.basicAck(deliveryTag,false,true);拒签,业务失败,拒签
     *
     */
    @PostConstruct //MyRabbitConfig对象创建完全以后，执行这个方法
    public void initRabbitTemplate(){
        /*
            correlationData:当前消息的唯一关联数据(这个是消息的唯一id)
            ack:消息是否成功收到
            cause:失败的原因
         */
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {

        }));

        /*
            消息正确抵达队列进行回调
                只要消息没有投递到指定的队列，就触发这个失败回调
                message:投递失败的消息详细信息
                replyCode:回复的状态码
                replyText:回复的文本内容
                exchange:这个消息发给的哪个交换机
                routingKey:这个消息发给的哪个路由键

         */
        rabbitTemplate.setReturnCallback(((message, replyCode, replyText, exchange, routingKey) -> {

        }));
    }
}
