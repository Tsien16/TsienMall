package com.tsien.mall.constant;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/9 0009 0:47
 */

public enum PayPlatformEnum {

    /**
     * 1=支付宝
     */
    ALIPAY(1, "支付宝");

    PayPlatformEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private String value;
    private int code;

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }

    public static PayPlatformEnum codeOf(int code) {
        for (PayPlatformEnum payPlatformEnum : values()) {
            if (payPlatformEnum.getCode() == code) {
                return payPlatformEnum;
            }
        }
        throw new RuntimeException("没有找到对应的枚举值");
    }
}
