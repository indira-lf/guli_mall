package com.indiralf.guli_mall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.indiralf.common.to.SkuHasStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.indiralf.guli_mall.ware.entity.WareSkuEntity;
import com.indiralf.guli_mall.ware.service.WareSkuService;
import com.indiralf.common.utils.PageUtils;
import com.indiralf.common.utils.R;



/**
 * 商品库存
 *
 * @author indiralf
 * @email indiralf@163.com
 * @date 2021-11-07 15:04:57
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 查询是否有库存
     */
    @PostMapping("/hasstock")
    public R getSkuHasStock(@RequestBody List<Long> skuIds){
        List<SkuHasStockVo> vos  = wareSkuService.getSkuHasStock(skuIds);
        return R.ok().setData(vos);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:wmswaresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:wmswaresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wmsWareSku = wareSkuService.getById(id);

        return R.ok().put("wmsWareSku", wmsWareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:wmswaresku:save")
    public R save(@RequestBody WareSkuEntity wmsWareSku){
		wareSkuService.save(wmsWareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:wmswaresku:update")
    public R update(@RequestBody WareSkuEntity wmsWareSku){
		wareSkuService.updateById(wmsWareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:wmswaresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
