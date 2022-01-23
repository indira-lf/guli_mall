package com.indiralf.guli_mall.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.indiralf.guli_mall.ware.vo.MergeVo;
import com.indiralf.guli_mall.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.indiralf.guli_mall.ware.entity.PurchaseEntity;
import com.indiralf.guli_mall.ware.service.PurchaseService;
import com.indiralf.common.utils.PageUtils;
import com.indiralf.common.utils.R;



/**
 * 采购信息
 *
 * @author indiralf
 * @email indiralf@163.com
 * @date 2021-11-07 15:04:57
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService wmsPurchaseService;

    /**
     * 领取采购单
     * @param ids
     * @return
     */
    @PostMapping("/done")
    public R finish(@RequestBody PurchaseDoneVo doneVo){

        wmsPurchaseService.done(doneVo);

        return R.ok();
    }
    /**
     * 领取采购单
     * @param ids
     * @return
     */
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids){

        wmsPurchaseService.received(ids);

        return R.ok();
    }

    /**
     * 将采购需求生成采购单
     * @param mergeVo
     * @return
     */
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo){

        wmsPurchaseService.mergePurchase(mergeVo);
        return R.ok();
    }

    /**
     * 获取没有领取的采购单
     */
    @RequestMapping("/unreceive/list")
    //@RequiresPermissions("ware:wmspurchase:list")
    public R unreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = wmsPurchaseService.queryPageUnreceive(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:wmspurchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wmsPurchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:wmspurchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity wmsPurchase = wmsPurchaseService.getById(id);

        return R.ok().put("wmsPurchase", wmsPurchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:wmspurchase:save")
    public R save(@RequestBody PurchaseEntity wmsPurchase){
        wmsPurchase.setCreateTime(new Date());
        wmsPurchase.setUpdateTime(new Date());
		wmsPurchaseService.save(wmsPurchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:wmspurchase:update")
    public R update(@RequestBody PurchaseEntity wmsPurchase){
		wmsPurchaseService.updateById(wmsPurchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:wmspurchase:delete")
    public R delete(@RequestBody Long[] ids){
		wmsPurchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
