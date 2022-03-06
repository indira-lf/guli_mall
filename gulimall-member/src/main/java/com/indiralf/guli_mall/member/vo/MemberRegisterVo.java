package com.indiralf.guli_mall.member.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author
 * @time 2022/3/5 20:03
 * @Description- TODO
 */
@Data
public class MemberRegisterVo {

    private String userName;

    private String password;

    private String phone;

}
