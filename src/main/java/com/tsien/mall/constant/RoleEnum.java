package com.tsien.mall.constant;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/26 0026 13:01
 */

public enum RoleEnum {

    /**
     * 0=普通用户
     */
    ROLE_CUSTOMER(0),

    /**
     * 1=管理员
     */
    ROLE_ADMIN(1);

    private int code;

    RoleEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
