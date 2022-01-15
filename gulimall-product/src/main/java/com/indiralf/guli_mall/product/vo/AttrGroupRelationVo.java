package com.indiralf.guli_mall.product.vo;

import lombok.Data;

/**
 * @author
 * @time 2022/1/15 19:19
 * @Description- 收集属性和属性分组的id
 */
@Data
public class AttrGroupRelationVo {
    /**
     * 属性Id
     */
    private Long attrId;
    /**
     * 属性分组Id
     */
    private Long attrGroupId;
}
