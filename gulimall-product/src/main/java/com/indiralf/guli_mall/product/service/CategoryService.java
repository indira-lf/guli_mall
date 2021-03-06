package com.indiralf.guli_mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indiralf.common.utils.PageUtils;
import com.indiralf.guli_mall.product.entity.CategoryEntity;
import com.indiralf.guli_mall.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author indiralf
 * @email indiralf@163.com
 * @date 2021-10-31 16:50:21
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //查询所有分类以及子分类，以树形结构组装起来
    List<CategoryEntity> listWithTree();

    //删除
    void removeMenuByIds(List<Long> asList);

    //找到catelogId的完整路径
    Long[] findCatelogPath(Long catelogId);

    void updateCascade(CategoryEntity category);

    List<CategoryEntity> getLevelOneCategorys();

    Map<String, List<Catelog2Vo>> getCatalogJson();
}

