package com.indiralf.guli_mall.ware.vo;

import lombok.Data;

/**
 * @author
 * @time 2022/3/17 15:34
 * @Description- TODO
 */
@Data
public class LockStockResult {

    private Long skuId;

    private Integer num;

    private Boolean locked;
}
