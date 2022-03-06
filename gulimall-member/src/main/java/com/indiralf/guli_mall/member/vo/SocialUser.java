package com.indiralf.guli_mall.member.vo;

import lombok.Data;

/**
 * @author
 * @time 2022/3/6 18:41
 * @Description- TODO
 */
@Data
public class SocialUser {

    private String access_token;

    private String remind_in;

    private long expires_in;

    private String uid;

    private String isRealName;
}
