package com.indiralf.guli_mall.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.annotation.PostConstruct;

/**
 * 使用RabbitMQ
 *  1、引入amqp(RabbitAutoConfiguration自动生效)
 *  2、给容器中自动配置了RabbitTemplate、CachingConnectionFactory、RabbitMessagingTemplate
 *  3、给配置文件中配置spring.rabbitMQ消息
 *  4、@EnableRabbit:开启功能
 *  5、监听消息:使用@RabbitListener；必须有@EnableRabbit
 */
@EnableRedisHttpSession
@EnableRabbit
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class, args);
    }

}
