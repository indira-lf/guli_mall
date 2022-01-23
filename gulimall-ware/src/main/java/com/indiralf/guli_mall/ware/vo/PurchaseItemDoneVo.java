package com.indiralf.guli_mall.ware.vo;

import lombok.Data;

/**
 * @author
 * @time 2022/1/23 16:58
 * @Description- TODO
 */
@Data
public class PurchaseItemDoneVo {

    private Long itemId;

    private Integer status;

    private String reason;
}
