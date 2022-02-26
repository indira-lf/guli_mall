package com.indiralf.guli_mall.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author
 * @time 2022/2/26 13:08
 * @Description- TODO
 */
@Data
@ToString
public class SpuItemAttrGroupVo{
    private String groupName;

    private List<Attr> attrs;
}
