package com.indiralf.guli_mall.product.vo;

import com.indiralf.guli_mall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author
 * @time 2022/1/16 9:50
 * @Description- TODO
 */
@Data
public class AttrGroupWithAttrsVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;
    /**
     * 封装实体信息
     */
    private List<AttrEntity> attrs;
}
