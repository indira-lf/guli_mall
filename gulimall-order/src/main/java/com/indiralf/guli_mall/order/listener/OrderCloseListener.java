package com.indiralf.guli_mall.order.listener;

import com.indiralf.guli_mall.order.entity.OrderEntity;
import com.indiralf.guli_mall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


/**
 * @author
 * @time 2022/3/19 13:47
 * @Description- TODO
 */
@Service
@RabbitListener(queues = "order.release.order.queue")
public class OrderCloseListener {

    @Autowired
    OrderService orderService;

    @RabbitHandler
    public void listener(OrderEntity entity, Channel channel, Message message) throws IOException {
        try {

            orderService.closeOrder(entity);
        }catch (Exception e){

            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(),true);
    }
}
