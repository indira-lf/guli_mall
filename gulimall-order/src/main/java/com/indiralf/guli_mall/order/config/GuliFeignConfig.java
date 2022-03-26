package com.indiralf.guli_mall.order.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author
 * @time 2022/3/16 11:42
 * @Description- TODO
 */
@Configuration
public class GuliFeignConfig {

    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor(){
        return template -> {
            //RequestContextHolder拿到刚进来的这个请求
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            if (request != null){
                //同步请求头数据Cookie
                String cookie = request.getHeader("Cookie");
                //给新请求同步Cookie
                template.header("Cookie",cookie);
            }
        };
    }
}
