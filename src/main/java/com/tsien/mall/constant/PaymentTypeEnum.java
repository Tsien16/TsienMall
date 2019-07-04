package com.tsien.mall.constant;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/5 0005 1:53
 */

public enum PaymentTypeEnum {

    /**
     * 1=在线支付
     */
    ONLINE_PAY(1, "在线支付");

    PaymentTypeEnum(int code, String value) {
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

    public static PaymentTypeEnum codeOf(int code) {
        for (PaymentTypeEnum paymentTypeEnum : values()) {
            if (paymentTypeEnum.getCode() == code) {
                return paymentTypeEnum;
            }
        }
        throw new RuntimeException("没有找到对应的枚举值");
    }
}
