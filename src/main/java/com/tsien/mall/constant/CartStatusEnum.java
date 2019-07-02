package com.tsien.mall.constant;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/2 0002 16:38
 */

public enum CartStatusEnum {

    /**
     * 1=CHECKED
     */
    CHECKED(1, "CHECKED"),

    /**
     * 0=UN_CHECKED
     */
    UN_CHECKED(0, "UN_CHECKED");

    private int code;

    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    CartStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
