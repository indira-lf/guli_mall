package com.feng.gulimall.auth.feign;

import com.indiralf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author
 * @time 2022/2/27 15:38
 * @Description- TODO
 */
@FeignClient("gulimall-third-party")
public interface ThirdFeignService {

    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);

}
