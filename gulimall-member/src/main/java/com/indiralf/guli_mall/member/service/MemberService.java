package com.indiralf.guli_mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indiralf.common.utils.PageUtils;
import com.indiralf.guli_mall.member.entity.MemberEntity;

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
}

