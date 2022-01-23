package com.indiralf.guli_mall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author
 * @time 2022/1/23 16:55
 * @Description- TODO
 */
@Data
public class PurchaseDoneVo {
    /**
     * 采购单id
     */
    @NotNull
    private Long id;
    /**
     * 采购项
     */
    private List<PurchaseItemDoneVo> items;
}
