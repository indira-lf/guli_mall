package com.feng.gulimall.auth.feign;

import com.feng.gulimall.auth.vo.SocialUser;
import com.feng.gulimall.auth.vo.UserLoginVo;
import com.feng.gulimall.auth.vo.UserRegisterVo;
import com.indiralf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**r
 * @author
 * @time 2022/3/6 9:46
 * @Description- TODO
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {

    @PostMapping("/member/member/oauth2/login")
    R oauthLogin(@RequestBody SocialUser socialUser) throws Exception;


    @PostMapping("/member/member/register")
    R register(@RequestBody UserRegisterVo vo);

    @PostMapping("/member/member/login")
    R login(@RequestBody UserLoginVo vo);
}
