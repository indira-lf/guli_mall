package com.indiralf.guli_mall.order.vo;

import lombok.Data;

import java.util.List;

/**
 * @author
 * @time 2022/3/17 15:29
 * @Description- TODO
 */
@Data
public class WareSkuLockVo {
    /**
     * 订单号
     */
    private String orderSn;
    /**
     * 需要锁住的所有库存信息
     */
    private List<OrderItemVo> locks;
}
