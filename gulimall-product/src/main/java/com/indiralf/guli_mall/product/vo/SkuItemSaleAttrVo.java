package com.indiralf.guli_mall.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @author
 * @time 2022/2/26 13:17
 * @Description- TODO
 */
@Data
public class SkuItemSaleAttrVo{
    private Long attrId;

    private String attrName;

    private List<AttrValueWithSkuIdVo> attrValues;
}
