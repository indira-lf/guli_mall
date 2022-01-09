package com.indiralf.common.Exception;

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
 */
public enum BizCodeEnume {
    /**
     * 系统未知异常
     */
    UNKNOWN_EXCEPTION(10000,"系统未知异常"),
    /**
     * 参数格式校验失败
     */
    VALID_EXCEmPTION(10001,"参数格式校验失败");

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