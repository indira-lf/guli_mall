package com.indiralf.guli_mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indiralf.common.to.SkuHasStockVo;
import com.indiralf.common.to.mq.OrderTo;
import com.indiralf.common.to.mq.StockLockedTo;
import com.indiralf.common.utils.PageUtils;
import com.indiralf.guli_mall.ware.entity.WareSkuEntity;
import com.indiralf.guli_mall.ware.vo.LockStockResult;
import com.indiralf.guli_mall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author indiralf
 * @email indiralf@163.com
 * @date 2021-11-07 15:04:57
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVo vo);

    void unlockStock(StockLockedTo to);

    void unlockStock(OrderTo orderTo);
}

