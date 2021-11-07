package com.indiralf.guli_mall.member.dao;

import com.indiralf.guli_mall.member.entity.MemberLoginLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员登录记录
 * 
 * @author indiralf
 * @email indiralf@163.com
 * @date 2021-11-07 14:52:49
 */
@Mapper
public interface MemberLoginLogDao extends BaseMapper<MemberLoginLogEntity> {
	
}
