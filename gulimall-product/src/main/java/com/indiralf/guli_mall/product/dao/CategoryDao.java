package com.indiralf.guli_mall.product.dao;

import com.indiralf.guli_mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author indiralf
 * @email indiralf@163.com
 * @date 2021-10-31 16:50:21
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
