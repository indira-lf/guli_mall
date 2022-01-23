package com.indiralf.guli_mall.ware.feign;

import com.indiralf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author
 * @time 2022/1/23 17:52
 * @Description- TODO
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    /**
     * 1、让所有请求过网关
     *      @FeignClient("gulimall-gateway")
     *      /api/product/skuinfo/info/{skuId}
     * 2、直接让后台指定服务处理
     *      @FeignClient("gulimall-product")
     *      /product/skuinfo/info/{skuId}
     * @param skuId
     * @return
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
