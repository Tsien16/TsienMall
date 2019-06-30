package com.tsien.mall.constant;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/1 0001 1:23
 */

public enum ProductStatusEnum {

    /**
     * 1=在售
     */
    ON_SALE(1, "在售");

    private int code;
    private String value;

    ProductStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

}
