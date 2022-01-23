package com.indiralf.common.constant;

/**
 * @author
 * @time 2022/1/23 15:59
 * @Description- TODO
 */
public class WareConstant {
    public enum PurchaseStatusEnum{
        /**
         * 新建
         */
        CREATED(0,"新建"),
        /**
         * 已分配
         */
        ASSIGNED(1,"已分配"),
        /**
         * 已领取
         */
        RECEIVE(2,"已领取"),
        /**
         * 已完成
         */
        FINISH(3,"已完成"),
        /**
         * 有异常
         */
        HASEORROR(4,"有异常");

        private int code;
        private String msg;

        PurchaseStatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

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
    }

    public enum PurchaseDetailStatusEnum{
        /**
         * 新建
         */
        CREATED(0,"新建"),
        /**
         * 已分配
         */
        ASSIGNED(1,"已分配"),
        /**
         * 正在采购
         */
        BUYING(2,"正在采购"),
        /**
         * 已完成
         */
        FINISH(3,"已完成"),
        /**
         * 采购失败
         */
        HASEORROR(4,"采购失败");

        private int code;
        private String msg;

        PurchaseDetailStatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

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
    }
}
