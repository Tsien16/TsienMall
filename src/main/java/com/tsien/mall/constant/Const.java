package com.tsien.mall.constant;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/23 0023 14:29
 */

public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public static final String NULL = "null";

    public static final Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");

    public interface CartLimit {

        /**
         * 限制库存失败
         */
        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";

        /**
         * 限制库存成功
         */
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

}
