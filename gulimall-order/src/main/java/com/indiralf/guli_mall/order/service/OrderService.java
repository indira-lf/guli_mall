package com.indiralf.guli_mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indiralf.common.utils.PageUtils;
import com.indiralf.guli_mall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author indiralf
 * @email indiralf@163.com
 * @date 2021-11-07 14:58:50
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

