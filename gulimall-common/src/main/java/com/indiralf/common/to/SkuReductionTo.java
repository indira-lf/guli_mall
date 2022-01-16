package com.indiralf.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author
 * @time 2022/1/16 15:52
 * @Description- TODO
 */
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrices;
}
