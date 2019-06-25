package com.tsien.mall.constant;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/23 0023 13:07
 */

public enum ResponseCodeEnum {

    /**
     * 0=成功
     */
    SUCCESS(0, "SUCCESS"),

    /**
     * 1=错误
     */
    ERROR(1, "ERROR"),

    /**
     * 2=参数错误
     */
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),

    /**
     * 10=需要登陆
     */
    NEED_LOGIN(10, "NEED_LOGIN");

    private final int code;
    private final String desc;

    ResponseCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
