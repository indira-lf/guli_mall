package com.indiralf.guli_mall.ware.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * @author
 * @time 2022/3/18 9:28
 * @Description- TODO
 */
@Configuration
public class MySeataConfig {

//    @Autowired
//    DataSourceProperties dataSourceProperties;
//
//    @Bean
//    public DataSource dataSource(DataSourceProperties dataSourceProperties){
//
//        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
//        if (StringUtils.hasText(dataSourceProperties.getName())){
//            dataSource.setPoolName(dataSourceProperties.getName());
//        }
//        return new DataSourceProxy(dataSource);
//    }
}
