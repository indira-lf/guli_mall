package com.indiralf.guli_mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author
 * @time 2022/1/23 15:51
 * @Description- TODO
 */
@Data
public class MergeVo {
    /**
     * 整单id
     */
    private Long purchaseId;
    /**
     * 合并项集合
     */
    private List<Long> items;
}
