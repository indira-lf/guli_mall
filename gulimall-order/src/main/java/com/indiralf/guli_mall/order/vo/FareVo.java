package com.indiralf.guli_mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author
 * @time 2022/3/17 10:45
 * @Description- TODO
 */
@Data
public class FareVo {

    private MemberAddressVo address;

    private BigDecimal fare;
}
