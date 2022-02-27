package com.feng.gulimall.auth.controller;

import com.feng.gulimall.auth.feign.ThirdFeignService;
import com.indiralf.common.Exception.BizCodeEnume;
import com.indiralf.common.constant.AuthServerConstant;
import com.indiralf.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @time 2022/2/27 13:19
 * @Description- TODO
 */
@Controller
public class LoginController {

    /**
     * 发送一个请求直接跳转到一个页面
     * SpringMVC viewcontroller:将请求和页面映射过来
     */

    @Autowired
    ThirdFeignService thirdFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @ResponseBody
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone){

        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisCode)){
            Long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis()- 1 < 60000){
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(),BizCodeEnume.SMS_CODE_EXCEPTION.getMsg());
            }
        }

        String code = UUID.randomUUID().toString().substring(0, 5)+"_"+System.currentTimeMillis();

        //redis缓存验证码
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+phone,code,10, TimeUnit.SECONDS);

        thirdFeignService.sendCode(phone,code);
        return R.ok();
    }
}
