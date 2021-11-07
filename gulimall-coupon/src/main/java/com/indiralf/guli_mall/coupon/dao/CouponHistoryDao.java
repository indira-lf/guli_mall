package com.indiralf.guli_mall.coupon.dao;

import com.indiralf.guli_mall.coupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author indiralf
 * @email indiralf@163.com
 * @date 2021-11-07 14:42:23
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}
