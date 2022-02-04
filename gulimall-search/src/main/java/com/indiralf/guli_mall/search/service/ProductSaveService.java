package com.indiralf.guli_mall.search.service;

import com.indiralf.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author
 * @time 2022/2/4 10:09
 * @Description- TODO
 */
public interface ProductSaveService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
