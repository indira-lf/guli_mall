package com.indiralf.guli_mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indiralf.common.utils.PageUtils;
import com.indiralf.guli_mall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author indiralf
 * @email indiralf@163.com
 * @date 2021-10-31 16:50:21
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

