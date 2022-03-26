package com.indiralf.guli_mall.order.to;

import com.indiralf.guli_mall.order.entity.OrderEntity;
import com.indiralf.guli_mall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author
 * @time 2022/3/17 10:29
 * @Description- TODO
 */
@Data
public class OrderCreatTo {

    private OrderEntity order;

    private List<OrderItemEntity> orderItem;

    private BigDecimal payPrice;

    private BigDecimal fare;
}
