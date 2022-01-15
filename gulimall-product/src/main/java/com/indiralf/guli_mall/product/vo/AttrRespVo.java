package com.indiralf.guli_mall.product.vo;

import lombok.Data;

/**
 * @author
 * @time 2022/1/15 13:47
 * @Description- TODO
 */
@Data
public class AttrRespVo extends AttrVo {
    /**
     * 所属分类名字
     */
    private String catelogName;
    /**
     * 所属分组名字
     */
    private String groupName;
    /**
     * 路径
     */
    private Long[] catelogPath;
}
