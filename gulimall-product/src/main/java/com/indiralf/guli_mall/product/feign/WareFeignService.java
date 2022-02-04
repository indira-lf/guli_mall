package com.indiralf.guli_mall.product.feign;

import com.indiralf.common.to.SkuHasStockVo;
import com.indiralf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author
 * @time 2022/2/3 20:00
 * @Description- TODO
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {

    /**
     * R在设计的时候可以加上泛型
     * 直接返回我们想要的结果
     * 自己封装解析结果
     * @param skuIds
     * @return
     */
    @PostMapping("/ware/waresku/hasstock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);
}
