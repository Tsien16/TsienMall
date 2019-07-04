package com.tsien.mall.constant;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/5 0005 1:50
 */

public enum OrderStatusEnum {

    /**
     * 已取消
     */
    CANCELED(0, "已取消"),

    /**
     * 未支付
     */
    NO_PAY(10, "未支付"),

    /**
     * 已付款
     */
    PAID(20, "已付款"),

    /**
     * 已发货
     */
    SHIPPED(40, "已发货"),

    /**
     * 订单完成
     */
    ORDER_SUCCESS(50, "订单完成"),

    /**
     * 订单关闭
     */
    ORDER_CLOSE(60, "订单关闭");

    OrderStatusEnum(int code, String value) {
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

    public static OrderStatusEnum codeOf(int code) {
        for (OrderStatusEnum orderStatusEnum : values()) {
            if (orderStatusEnum.getCode() == code) {
                return orderStatusEnum;
            }
        }
        throw new RuntimeException("没有找到对应的枚举值");
    }
}
