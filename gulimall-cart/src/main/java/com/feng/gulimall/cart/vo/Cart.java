package com.feng.gulimall.cart.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author
 * @time 2022/3/12 21:42
 * @Description- 整个购物车
 */
public class Cart {
    /**
     * 商品项
     */
    List<CartItem> items;
    /**
     * 商品数量
     */
    private Integer countNum;
    /**
     * 商品类型数量
     */
    private Integer countType;
    /**
     * 商品总价
     */
    private BigDecimal totalAmount;
    /**
     * 优惠
     */
    private BigDecimal reduce = BigDecimal.ZERO;

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Integer getCountNum() {
        int count = 0;
        if (items != null && items.size() > 0){
            for (CartItem item : items) {
                count += item.getCount();
            }
        }
        return count;
    }

    public Integer getCountType() {
        int count = 0;
        if (items != null && items.size() > 0){
            for (CartItem item : items) {
                count += 1;
            }
        }
        return count;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        if (items != null && items.size() > 0){
            for (CartItem item : items) {
                BigDecimal totalPrice = item.getTotalPrice();
                amount.add(totalPrice);
            }
        }

        BigDecimal subtract = amount.subtract(getReduce());

        return subtract;
    }


    public BigDecimal getReduce() {
        return reduce;
    }

}
