package com.feng.gulimall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.feng.gulimall.auth.feign.MemberFeignService;
import com.feng.gulimall.auth.feign.ThirdFeignService;
import com.feng.gulimall.auth.vo.MemberRespVo;
import com.feng.gulimall.auth.vo.UserLoginVo;
import com.feng.gulimall.auth.vo.UserRegisterVo;
import com.indiralf.common.exception.BizCodeEnume;
import com.indiralf.common.constant.AuthServerConstant;
import com.indiralf.common.constant.BizCodeConstant;
import com.indiralf.common.utils.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.indiralf.common.constant.AuthServerConstant.LOGIN_USER;

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

    @Autowired
    MemberFeignService memberFeignService;

    @ResponseBody
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone){

        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(redisCode)){
            Long l = Long.parseLong(redisCode.split(BizCodeConstant.UNDERSCORE)[1]);
            if (System.currentTimeMillis()- 1 < BizCodeConstant.SIXTY_THOUSAND){
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(),BizCodeEnume.SMS_CODE_EXCEPTION.getMsg());
            }
        }

        String code = UUID.randomUUID().toString().substring(0, 5);
        String subString = code + "_"+System.currentTimeMillis();

        //redis缓存验证码
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+phone,subString,10, TimeUnit.SECONDS);

        thirdFeignService.sendCode(phone,code);
        return R.ok();
    }

    /**
     * //TODO 重定向携带数据，利用session原理。将数据放在session中。只要跳到下一个页面取出这个数据以后，session里面的数据就会删掉
     * //TODO 分布式session
     * @param vo
     * @param result
     * @param redirectAttributes 重定向携带数据
     * @return
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterVo vo, BindingResult result, RedirectAttributes redirectAttributes){
        if (result.hasErrors()){

            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField,FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors",errors);
            //Request method 'POST' not supported

            //校验出错，转发到注册页
//            return "forward:/reg.html";
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        //调用远程服务
        //1、校验验证码
        String code = vo.getCode();
        String s = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
        if (StringUtils.isNotBlank(s)){
            if (code.equals(s.split(BizCodeConstant.UNDERSCORE)[0])){
                //删除验证码
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                //验证码通过
                R r = memberFeignService.register(vo);
                if (r.getCode() == 0){
                    redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());
                    return "redirect:http://auth.gulimall.com/login.html";
                }else {
                    HashMap<String, String> errors = new HashMap<>(16);
                    errors.put("msg",r.getData("msg",new TypeReference<String>(){}));
                    redirectAttributes.addFlashAttribute("errors",errors);
                    return "redirect:http://auth.gulimall.com/reg.html";
                }
            }else {
                HashMap<String, String> errors = new HashMap<>(16);
                errors.put("code","验证码错误");
                redirectAttributes.addFlashAttribute("errors",errors);
                return "redirect:http://auth.gulimall.com/reg.html";
            }
        }else {
            HashMap<String, String> errors = new HashMap<>(16);
            errors.put("code","验证码错误");
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimall.com/reg.html";
        }

        //注册成功返回到首页
//        return "redirect:/login.html";
    }

    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes,
                        HttpSession session){

        R login = memberFeignService.login(vo);
        if (login.getCode() == 0){
            MemberRespVo data = login.getData("data", new TypeReference<MemberRespVo>() {
            });
            session.setAttribute(LOGIN_USER,data);
            return "redirect:http://gulimall.com";
        }else {
            HashMap<String, String> errors = new HashMap<>(16);
            errors.put("msg",login.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }

    @GetMapping("login.html")
    public String loginPage(HttpSession session){
        Object attribute = session.getAttribute(LOGIN_USER);
        if (attribute == null){
            return "login";
        }else {
            return "redirect:http://gulimall.com";
        }
    }
}
