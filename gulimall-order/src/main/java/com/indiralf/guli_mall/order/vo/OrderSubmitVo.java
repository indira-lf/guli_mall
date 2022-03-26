package com.indiralf.guli_mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author
 * @time 2022/3/17 9:43
 * @Description- 订单提交数据
 */
@Data
public class OrderSubmitVo {
    /**
     * 收货地址ID
     */
    private  Long addrId;
    /**
     * 支付方式
     */
    private Integer payType;
    /**
     * 防重令牌
     */
    private String orderToken;
    /**
     * 应付价格
     */
    private BigDecimal payPrice;
    /**
     * 备注
     */
    private BigDecimal note;
}
