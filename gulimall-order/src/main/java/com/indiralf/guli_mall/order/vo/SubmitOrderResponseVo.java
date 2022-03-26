package com.indiralf.guli_mall.order.vo;

import com.indiralf.guli_mall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @author
 * @time 2022/3/17 10:10
 * @Description- TODO
 */
@Data
public class SubmitOrderResponseVo {

    private OrderEntity order;

    /**
     * 0成功
     */
    private Integer code;
}
