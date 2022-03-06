package com.indiralf.guli_mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indiralf.common.utils.PageUtils;
import com.indiralf.guli_mall.member.entity.MemberEntity;
import com.indiralf.guli_mall.member.exception.PhoneExistException;
import com.indiralf.guli_mall.member.exception.UserNameExistException;
import com.indiralf.guli_mall.member.vo.MemberLoginVo;
import com.indiralf.guli_mall.member.vo.MemberRegisterVo;
import com.indiralf.guli_mall.member.vo.SocialUser;

import java.util.Map;

/**
 * 会员
 *
 * @author indiralf
 * @email indiralf@163.com
 * @date 2021-11-07 14:52:49
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberRegisterVo vo);

    void checkPhoneUnique(String phone) throws PhoneExistException;

    void checkUserNameUnique(String userName) throws UserNameExistException;

    MemberEntity login(MemberLoginVo vo);

    MemberEntity login(SocialUser socialUser);
}

