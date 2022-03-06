package com.indiralf.guli_mall.member.exception;

/**
 * @author
 * @time 2022/3/5 21:41
 * @Description- TODO
 */
public class UserNameExistException extends RuntimeException{
    public UserNameExistException() {
        super("用户名存在");
    }
}
