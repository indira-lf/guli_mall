package com.indiralf.guli_mall.ware.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author
 * @time 2022/3/16 10:08
 * @Description- TODO
 */
@Data
public class OrderItemVo {

    private Long skuId;

    private Boolean check;

    private String title;

    private String image;

    private List<String> skuAttr;

    private BigDecimal price;

    private Integer count;

    private boolean hasStock;

    private BigDecimal weight;

}
