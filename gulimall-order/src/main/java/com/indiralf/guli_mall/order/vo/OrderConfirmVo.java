package com.indiralf.guli_mall.order.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @time 2022/3/16 10:03
 * @Description- 订单确认页需要的数据
 */
public class OrderConfirmVo {

    /**
     * 收货地址
     */
    List<MemberAddressVo> address;
    /**
     * 选中的购物项
     */
    List<OrderItemVo> items;
    /**
     * 发票信息
     */

    /**
     * 优惠劵
     */
    Integer integration;
    /**
     * 订单总额
     */
    BigDecimal total;
    /**
     * 应付价格
     */
    BigDecimal payPrice;

    /**
     * 防重令牌
     */
    String orderToken;

    @Getter @Setter
    Map<Long,Boolean> stocks;

    public Integer getCount(){
        Integer i = 0;
        if (items != null){
            for (OrderItemVo item : items) {
                i += item.getCount();
            }
        }
        return i;
    }

    public List<MemberAddressVo> getAddress() {
        return address;
    }

    public void setAddress(List<MemberAddressVo> address) {
        this.address = address;
    }

    public List<OrderItemVo> getItems() {
        return items;
    }

    public void setItems(List<OrderItemVo> items) {
        this.items = items;
    }

    public Integer getIntegration() {
        return integration;
    }

    public void setIntegration(Integer integration) {
        this.integration = integration;
    }

    public BigDecimal getTotal() {
        BigDecimal sum = new BigDecimal("0");
        if (items != null){
            for (OrderItemVo item : items) {
                BigDecimal multiply = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                sum = sum.add(multiply);
            }
        }
        return sum;
    }

    public BigDecimal getPayPrice() {
        return getTotal();
    }

    public String getOrderToken() {
        return orderToken;
    }

    public void setOrderToken(String orderToken) {
        this.orderToken = orderToken;
    }
}
