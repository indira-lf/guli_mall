package com.indiralf.guli_mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 1、整合MyBatis-Plus
 *      1)导入依赖
 *      2)配置
 *          配置数据源
 *          配置Mybatis—plus
 *              使用 @MapperScan
 *              告诉myBatis映射文件位置 yml文件配置
 *
 * JSR303
 *  1)、给Bean添加校验注解 javax.validation.constraints,并定义自己的message提示
 *  2)、开启校验功能@Valid
 *
 */
@MapperScan("com.indiralf.guli_mall.product.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
