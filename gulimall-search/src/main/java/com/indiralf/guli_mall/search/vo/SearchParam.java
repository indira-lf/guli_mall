package com.indiralf.guli_mall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @author
 * @time 2022/2/13 13:23
 * @Description- 封装页面可能传过来的id
 */
@Data
public class SearchParam {

    /**
     * 关键字
     */
    private String keyword;
    /**
     * 三级分类id
     */
    private String catalog3Id;
    /**
     * 排序
     * sort=saleCount_asc/desc
     * sort=skuCount_asc/desc
     * sort=hotCount_asc/desc
     */
    private String sort;
    /**
     * 是否只显示有货
     */
    private Integer hasStock = 1;//默认有库存
    /**
     * 价格区间
     */
    private String skuPrice;
    /**
     * 品牌id
     */
    private List<Long> brandId;
    /**
     * 属性
     */
    private List<String> attrs;
    /**
     * 页码
     */
    private Integer pageNum = 1;
    /**
     * 原url
     */
    private String _queryString;

}
