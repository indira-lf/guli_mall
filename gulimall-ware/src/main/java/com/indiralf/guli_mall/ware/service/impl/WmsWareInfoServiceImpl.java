package com.indiralf.guli_mall.ware.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indiralf.common.utils.PageUtils;
import com.indiralf.common.utils.Query;

import com.indiralf.guli_mall.ware.dao.WmsWareInfoDao;
import com.indiralf.guli_mall.ware.entity.WmsWareInfoEntity;
import com.indiralf.guli_mall.ware.service.WmsWareInfoService;


@Service("wmsWareInfoService")
public class WmsWareInfoServiceImpl extends ServiceImpl<WmsWareInfoDao, WmsWareInfoEntity> implements WmsWareInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WmsWareInfoEntity> page = this.page(
                new Query<WmsWareInfoEntity>().getPage(params),
                new QueryWrapper<WmsWareInfoEntity>()
        );

        return new PageUtils(page);
    }

}