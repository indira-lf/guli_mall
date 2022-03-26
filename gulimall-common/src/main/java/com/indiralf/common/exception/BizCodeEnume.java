package com.indiralf.common.exception;

/**
 * @author
 * @time 2022/1/8 18:24
 * @Description- 错误码和错误信息定义类
 *
 * 1.错误码定义规则为5位数字
 * 2.前两位表示业务场景，最后三位表示错误码。例如：10001。10：通用 001：系统未知异常
 * 3.错误描述
 *      10: 通用
 *          001: 参数格式校验
 *      11: 商品
 *      12: 订单
 *      13: 购物车
 *      14: 物流
 *      15: 用户
 *      21: 库存
 */
public enum BizCodeEnume {
    /**
     * 短信验证码评率太高
     */
    UNKNOWN_EXCEPTION(10000,"系统未知异常"),
    /**
     * 系统未知异常
     */
    SMS_CODE_EXCEPTION(10002,"验证码获取评率太高，稍后再试"),
    /**
     * 参数格式校验失败
     */
    VALID_EXCEPTION(10001,"参数格式校验失败"),
    /**
     * 商品上架异常
     */
    PRODUCT_UP_EXCEPTION(11000,"商品上架异常"),
    /**
     * 用户存在
     */
    USER_EXIST_EXCEPTION(15001,"用户存在"),
    /**
     * 手机号存在
     */
    PHONE_EXIST_EXCEPTION(15002,"手机号存在"),
    /**
     * 账号或密码错误
     */
    LOGINACCT_PASSWORD_INVALID_EXCEPTION(15003,"账号或密码错误"),
    /**
     * 商品库存不足
     */
    NO_STOCK_EXCEPTION(21000,"商品库存不足")
    ;

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    BizCodeEnume(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
