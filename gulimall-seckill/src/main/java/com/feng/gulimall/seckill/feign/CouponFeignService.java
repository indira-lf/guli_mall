package com.feng.gulimall.seckill.feign;

import com.indiralf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author
 * @time 2022/3/27 13:02
 * @Description- TODO
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {

    @GetMapping("/coupon/seckillsession/late3DaysSession")
     R getLate3DaysSession();
}
