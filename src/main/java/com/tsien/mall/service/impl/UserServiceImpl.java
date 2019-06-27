package com.tsien.mall.service.impl;

import com.tsien.mall.constant.Const;
import com.tsien.mall.constant.RoleEnum;
import com.tsien.mall.dao.UserMapper;
import com.tsien.mall.model.UserDO;
import com.tsien.mall.service.UserService;
import com.tsien.mall.util.MD5Util;
import com.tsien.mall.util.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

}
