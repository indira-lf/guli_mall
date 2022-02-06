package com.indiralf.guli_mall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

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
 *  模板引擎
 *      1) 关闭缓存  thymeleaf:
 *                  cache: false
 *      2) 静态资源都放在static文件夹下就可以安装路径直径直接访问
 *      3) 页面放在templates下，直接访问
 *          springboot访问项目的时候会直接找index
 *      4) 页面修改不重启服务器实时更新
 *          4.1 引入devtools依赖
 *          4.2 修改完页面 ctrl+shift+F9重新自动编译页面
 *
 */
@EnableFeignClients(basePackages = "com.indiralf.guli_mall.product.feign")
@MapperScan("com.indiralf.guli_mall.product.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
