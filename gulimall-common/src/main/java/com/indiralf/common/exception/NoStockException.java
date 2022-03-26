package com.indiralf.common.exception;

/**
 * @author
 * @time 2022/3/17 16:10
 * @Description- TODO
 */
public class NoStockException extends RuntimeException{

    private Long skuId;
    public NoStockException(Long skuId){
        super("商品【"+skuId+"】没有足够的库存");
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
