package com.indiralf.guli_mall.search.feign;

import com.indiralf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author
 * @time 2022/2/19 20:46
 * @Description- TODO
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    @GetMapping("/product/attr/info/{attrId}")
    public R attrsInfo(@PathVariable("attrId")Long attrId);

    @GetMapping("/product/brand/infos")
    public R brandsInfo(@PathVariable("brandIds") List<Long> brandIds);
}
