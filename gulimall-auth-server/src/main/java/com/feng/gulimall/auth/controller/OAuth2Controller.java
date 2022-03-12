package com.feng.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.feng.gulimall.auth.feign.MemberFeignService;
import com.feng.gulimall.auth.vo.MemberRespVo;
import com.feng.gulimall.auth.vo.SocialUser;
import com.indiralf.common.constant.BizCodeConstant;
import com.indiralf.common.utils.HttpUtils;
import com.indiralf.common.utils.R;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

import static com.indiralf.common.constant.AuthServerConstant.LOGIN_USER;

/**
 * @author
 * @time 2022/3/6 15:28
 * @Description- 社交登录请求
 */
@Controller
public class OAuth2Controller {

    @Autowired
    MemberFeignService memberFeignService;

    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session, HttpServletResponse servletResponse) throws Exception {

        HashMap<String, String> map = new HashMap<>(16);
        map.put("client_id","2636917288");
        map.put("client_secret","6a263e9284c6c1a74a62eadacc11b6e2");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://auth.gulimall.com/oauth2.0/weibo/success");
        map.put("code",code);
        HttpResponse response = HttpUtils.doPost("api.weibo.com", "/oauth2/access_token", "post", null, null, map);

        if (response.getStatusLine().getStatusCode() == BizCodeConstant.SUCCESS){
            //获取到了accessToken
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
            //第一次进网站
            R oauthLogin = memberFeignService.oauthLogin(socialUser);
            if (oauthLogin.getCode() == 0){
                MemberRespVo data = oauthLogin.getData("data", new TypeReference<MemberRespVo>() {
                });
                /*
                    第一次使用session；命令浏览器保存卡号。JSESSIONID这个cookie
                    以后浏览器访问哪个网站就会带上这个网站的cookie
                    子域之间：gulimall.com auth.gulimall.com ...
                    发卡的时候指定域名为父域名，即使是子域系统发的卡，也能让父域直接使用
                 */
                //TODO 默认发的令牌。session=dsajkdjl。作用域：当前域；(解决子域session共享问题)
                //TODO 使用JSON的序列化方式来序列化对象数据到redis中
                session.setAttribute(LOGIN_USER,data);
                return "redirect:http://gulimall.com";
            }else {
                return "redirect:http://auth.gulimall.com/login.html";
            }
        }else {
            return "redirect:http://auth.gulimall.com/login.html";
        }

    }
}
