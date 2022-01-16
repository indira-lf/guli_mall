package com.indiralf.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author
 * @time 2022/1/16 15:36
 * @Description- TODO
 */
@Data
public class SpuBoundTo {
    /**
     *
     */
    private Long spuId;
    /**
     * 成长积分
     */
    private BigDecimal buyBounds;
    /**
     * 购物积分
     */
    private BigDecimal growBounds;
}
