package com.indiralf.guli_mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 1、整合MyBatis-Plus
 *      1)导入依赖
 *      2)配置
 *          配置数据源
 *          配置Mybatis—plus
 *              使用 @MapperScan
 *              告诉myBatis映射文件位置 yml文件配置
 */
@MapperScan("com.indiralf.guli_mall.product.dao")
@SpringBootApplication
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
