package com.indiralf.guli_mall.coupon.dao;

import com.indiralf.guli_mall.coupon.entity.CouponSpuRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券与产品关联
 * 
 * @author indiralf
 * @email indiralf@163.com
 * @date 2021-11-07 14:42:23
 */
@Mapper
public interface CouponSpuRelationDao extends BaseMapper<CouponSpuRelationEntity> {
	
}
