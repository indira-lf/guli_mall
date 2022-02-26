package com.indiralf.guli_mall.product.vo;

import com.indiralf.guli_mall.product.entity.SkuImagesEntity;
import com.indiralf.guli_mall.product.entity.SkuInfoEntity;
import com.indiralf.guli_mall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @author
 * @time 2022/2/26 12:12
 * @Description- TODO
 */
@Data
public class SkuItemVo {
    SkuInfoEntity info;

    List<SkuImagesEntity> images;

    List<SkuItemSaleAttrVo> saleAttr;

    SpuInfoDescEntity desp;

    List<SpuItemAttrGroupVo> groupAttrs;

    boolean hasStock = true;


}
