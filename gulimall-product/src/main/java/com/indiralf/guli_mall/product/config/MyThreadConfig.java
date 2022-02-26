package com.indiralf.guli_mall.product.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @time 2022/2/26 20:09
 * @Description- 线程池
 */
//@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
@Configuration
public class MyThreadConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool){
        return new ThreadPoolExecutor(pool.getCoreSize(),
                            pool.getMaxSize(),
                            pool.getKeepAliveTime(),
                            TimeUnit.SECONDS,
                            new LinkedBlockingDeque<>(100000),
                            Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
    }
}
