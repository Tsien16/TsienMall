package com.tsien.mall.service.impl;

import com.tsien.mall.constant.Const;
import com.tsien.mall.constant.RoleEnum;
import com.tsien.mall.dao.UserMapper;
import com.tsien.mall.model.UserDO;
import com.tsien.mall.service.UserService;
import com.tsien.mall.util.MD5Util;
import com.tsien.mall.util.ServerResponse;
import com.tsien.mall.util.TokenCacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/23 0023 12:26
 */

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录
     *
     * @param username username username
     * @param password password password
     * @return 登陆结果
     */
    @Override
    public ServerResponse<UserDO> login(String username, String password) {

        int resultCount = userMapper.countUsersByUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        // 密码通过MD5加密
        String md5Password = MD5Util.md5EncodeUtf8(password);

        UserDO userDO = userMapper.getUserByUsernameAndPassword(username, md5Password);
        if (userDO == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        userDO.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登陆成功", userDO);

    }

    /**
     * 用户注册
     *
     * @param userDO 用户实体
     * @return 注册结果
     */
    @Override
    public ServerResponse<String> register(UserDO userDO) {

        // 复用checkValid方法
        ServerResponse<String> validResponse = this.checkValid(userDO.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        validResponse = this.checkValid(userDO.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        // 设置角色为普通用户
        userDO.setRole(RoleEnum.ROLE_CUSTOMER.getCode());

        //密码MD5加密
        userDO.setPassword(MD5Util.md5EncodeUtf8(userDO.getPassword()));

        int resultCount = userMapper.insert(userDO);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");

    }

    /**
     * 校验注册时候用户名或者密码的合法性
     *
     * @param string 字符串
     * @param type   email/username
     * @return 校验结果
     */
    @Override
    public ServerResponse<String> checkValid(String string, String type) {
        if (StringUtils.isNotBlank(type)) {
            // 开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.countUsersByUsername(string);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }

            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.countUsersByEmail(string);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        return ServerResponse.createBySuccessMessage("校验成功");
    }

    /**
     * 获取找回密码提示的问题
     *
     * @param username username
     * @return 密码提示问题
     */
    @Override
    public ServerResponse<String> getQuestion(String username) {

        if (username == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        ServerResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String question = userMapper.getQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }

        return ServerResponse.createByErrorMessage("找回密码的问题是空的");

    }

    /**
     * 检查问题的答案
     *
     * @param username username
     * @param question question
     * @param answer   answer
     * @return 验证结果
     */
    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.countUsersByUsernameAndQuestionAndAnswer(username, question, answer);
        if (resultCount > 0) {
            // 说明问题及问题答案是这个用户的，并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            TokenCacheUtil.setKey(TokenCacheUtil.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }

        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    /**
     * 未登录状态下的修改密码
     *
     * @param username    username
     * @param newPassword newPassword
     * @param forgetToken forgetToken
     * @return 修改密码的结果
     */
    @Override
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，Token需要传递");
        }

        ServerResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }

        String token = TokenCacheUtil.getkey(TokenCacheUtil.TOKEN_PREFIX + username);

        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("Token无效或者过期");
        }

        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.md5EncodeUtf8(newPassword);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);

            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("Token错误，请重新获取重置密码的Token");
        }

        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    /**
     * 登陆状态下重置密码
     *
     * @param userDO      userDO
     * @param oldPassword oldPassword
     * @param newPassword newPassword
     * @return 重置密码的结果
     */
    @Override
    public ServerResponse<String> resetPassword(UserDO userDO, String oldPassword, String newPassword) {
        // 防止横向越权，要校验一下这个用户的旧密码，一定要指定是这个用户
        int resultCount = userMapper.countUsersByUserIdAndPassword(userDO.getId(), MD5Util.md5EncodeUtf8(oldPassword));
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        userDO.setPassword(MD5Util.md5EncodeUtf8(newPassword));

        int updateCount = userMapper.updateByPrimaryKey(userDO);

        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("更新密码成功");
        }

        return ServerResponse.createByErrorMessage("更新密码失败");

    }

    /**
     * 获取用户信息
     *
     * @param userId userId
     * @return 用户信息
     */
    @Override
    public ServerResponse<UserDO> getUserInfo(Integer userId) {

        UserDO userDO = userMapper.getByPrimaryKey(userId);
        if (userDO == null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }

        userDO.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess(userDO);

    }

    /**
     * 更新用户个人信息
     *
     * @param userDO userDO
     * @return 更新的结果
     */
    @Override
    public ServerResponse<UserDO> updateUserInfo(UserDO userDO) {

        // username 不能被更新
        // 校验新的Email是不是已经存在，并且存在的Email不能是当前这个用户的

        int resultCount = userMapper.countUsersByUserIdAndEmail(userDO.getId(), userDO.getEmail());

        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已经存在，请更换email后再尝试更新");
        }

        UserDO updateUser = new UserDO();
        updateUser.setId(userDO.getId());
        updateUser.setEmail(userDO.getEmail());
        updateUser.setPhone(userDO.getPhone());
        updateUser.setQuestion(userDO.getQuestion());
        updateUser.setAnswer(userDO.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);

        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        }

        return ServerResponse.createByErrorMessage("更新个人信息失败");

    }

}
