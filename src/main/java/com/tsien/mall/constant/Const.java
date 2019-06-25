package com.tsien.mall.constant;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/23 0023 14:29
 */

public class Const {

    public static final String CURRENT_USER = "currentUser";

    public interface Role{

        /**
         * 0=普通用户
         */
        int ROLE_CUSTOMER = 0;

        /**
         * 1=管理员
         */
        int ROLE_ADMIN = 1;
    }
}
