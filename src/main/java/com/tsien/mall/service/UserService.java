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

    /**
     * 用户注册
     *
     * @param userDO 用户实体
     * @return 注册结果
     */
    ServerResponse<String> register(UserDO userDO);

    /**
     * 校验注册时候用户名或者密码的合法性
     *
     * @param string 字符串
     * @param type   email/username
     * @return 校验结果
     */
    ServerResponse<String> checkValid(String string, String type);


}
