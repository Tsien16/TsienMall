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

    /**
     * 获取找回密码提示的问题
     *
     * @param username username
     * @return 密码提示问题
     */
    ServerResponse<String> getQuestion(String username);

    /**
     * 检查问题的答案
     *
     * @param username username
     * @param question question
     * @param answer   answer
     * @return 验证结果
     */
    ServerResponse<String> checkAnswer(String username, String question, String answer);

    /**
     * 未登录状态下的修改密码
     *
     * @param username    username
     * @param newPassword newPassword
     * @param forgetToken forgetToken
     * @return 修改密码的结果
     */
    ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken);

    /**
     * 登陆状态下重置密码
     *
     * @param userDO      userDO
     * @param oldPassword oldPassword
     * @param newPassword newPassword
     * @return 重置密码的结果
     */
    ServerResponse<String> resetPassword(UserDO userDO, String oldPassword, String newPassword);

    /**
     * 获取用户信息
     *
     * @param userId userId
     * @return 用户信息
     */
    ServerResponse<UserDO> getUserInfo(Integer userId);

    /**
     * 更新用户个人信息
     *
     * @param userDO userDO
     * @return 更新的结果
     */
    ServerResponse<UserDO> updateUserInfo(UserDO userDO);

    /**
     * 校验用户是否是管理员
     *
     * @param userDO userDO
     * @return 校验结果
     */
    ServerResponse checkUserRoleOfAdmin(UserDO userDO);


}
