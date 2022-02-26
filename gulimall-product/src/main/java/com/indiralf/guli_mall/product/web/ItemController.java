package com.indiralf.guli_mall.product.web;

import com.indiralf.guli_mall.product.service.SkuInfoService;
import com.indiralf.guli_mall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author
 * @time 2022/2/20 13:43
 * @Description- TODO
 */
@Controller
public class ItemController {

    @Autowired
    SkuInfoService skuInfoService;
    /**
     * 展示当前sku的详情
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model){

        SkuItemVo vo = skuInfoService.item(skuId);
        model.addAttribute("item",vo);
        return "item";
    }
}
