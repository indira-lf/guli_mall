package com.indiralf.guli_mall.search.vo;

import com.indiralf.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @author
 * @time 2022/2/13 13:51
 * @Description- 查询结果
 */
@Data
public class SearchResult {
    /**
     * 商品信息
     */
    private List<SkuEsModel> product;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 总记录数
     */
    private Long total;
    /**
     * 总页码
     */
    private Integer totalPages;

    /**
     * 品牌
     */
    private List<BrandVo> brands;
    /**
     * 属性
     */
    private List<AttrVo> attrs;
    /**
     * 分类
     */
    private List<CatalogVo> catalogs;

    @Data
    public static class BrandVo{

        private Long brandId;

        private String brandName;

        private String brandImg;

    }

    @Data
    public static class AttrVo{

        private Long attrId;

        private String attrName;

        private List<String> attrValue;
    }

    @Data
    public static class CatalogVo{

        private Long CatalogId;

        private String CatalogName;

    }
}
