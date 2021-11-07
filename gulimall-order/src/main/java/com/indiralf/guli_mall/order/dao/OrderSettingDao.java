package com.indiralf.guli_mall.order.dao;

import com.indiralf.guli_mall.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author indiralf
 * @email indiralf@163.com
 * @date 2021-11-07 14:58:50
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {
	
}
