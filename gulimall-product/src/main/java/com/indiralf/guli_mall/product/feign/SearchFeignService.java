package com.indiralf.guli_mall.product.feign;

import com.indiralf.common.to.es.SkuEsModel;
import com.indiralf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author
 * @time 2022/2/4 14:43
 * @Description- TODO
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {
    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
