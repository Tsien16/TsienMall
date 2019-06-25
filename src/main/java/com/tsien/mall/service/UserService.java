package com.tsien.mall.service;

import com.tsien.mall.model.UserDO;
import com.tsien.mall.util.ServerResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/23 0023 12:25
 */

public interface UserService {

    /**
     * 用户登陆
     *
     * @param username username
     * @param password password
     * @return 包含用户信息的response
     */
    ServerResponse<UserDO> login(String username, String password);
}
