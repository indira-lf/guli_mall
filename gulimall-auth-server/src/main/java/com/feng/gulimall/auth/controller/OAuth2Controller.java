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

import javax.servlet.http.HttpSession;
import java.util.HashMap;

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
    public String weibo(@RequestParam("code") String code, HttpSession session) throws Exception {

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

                session.setAttribute("loginUser",data);
                return "redirect:http://gulimall.com";
            }else {
                return "redirect:http://auth.gulimall.com/login.html";
            }
        }else {
            return "redirect:http://auth.gulimall.com/login.html";
        }

    }
}
