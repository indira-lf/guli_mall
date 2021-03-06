package com.indiralf.guli_mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indiralf.common.utils.PageUtils;
import com.indiralf.guli_mall.ware.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author indiralf
 * @email indiralf@163.com
 * @date 2021-11-07 15:04:57
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

