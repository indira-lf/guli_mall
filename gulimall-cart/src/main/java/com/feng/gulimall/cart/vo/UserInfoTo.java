package com.feng.gulimall.cart.vo;

import lombok.Data;

/**
 * @author
 * @time 2022/3/13 9:59
 * @Description- TODO
 */
@Data
public class UserInfoTo {

    private Long UserId;

    private String userKey;

    private boolean tempUser = false;
}
