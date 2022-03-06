package com.indiralf.guli_mall.member.exception;

/**
 * @author
 * @time 2022/3/5 21:42
 * @Description- TODO
 */
public class PhoneExistException extends RuntimeException{
    public PhoneExistException() {
        super("手机号存在");
    }
}
