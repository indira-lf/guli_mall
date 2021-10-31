package com.indiralf.guli_mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indiralf.common.utils.PageUtils;
import com.indiralf.guli_mall.product.entity.SpuImagesEntity;

import java.util.Map;

/**
 * spu图片
 *
 * @author chenshun
 * @email indiralf@163.com
 * @date 2021-10-31 16:50:21
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

