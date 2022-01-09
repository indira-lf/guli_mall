package com.indiralf.guli_mall.product.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author
 * @time 2022/1/9 14:31
 * @Description- TODO
 */
@Configuration
@EnableTransactionManagement //开启使用
@MapperScan("com.indiralf.guli_mall.product.dao")
public class MyBatisConfig {

    //引入分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        //设置请求的页面大于最大页操作，true调回到首页，false继续请求，默认false
        paginationInterceptor.setOverflow(true);
        //设置最大单页限制数量，默认500 条， -1不受限制
        paginationInterceptor.setLimit(1000);

        return paginationInterceptor;
    }
}
