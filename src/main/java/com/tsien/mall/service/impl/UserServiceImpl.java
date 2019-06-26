package com.tsien.mall.service.impl;

import com.tsien.mall.constant.Const;
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

        int resultCount = userMapper.countUsersByUsername(userDO.getUsername());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("用户名已存在");
        }

        resultCount = userMapper.countUsersByEmail(userDO.getEmail());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("邮箱已存在");
        }

        // 设置角色为普通用户
        userDO.setRole(Const.Role.ROLE_CUSTOMER);

        //密码MD5加密
        userDO.setPassword(MD5Util.md5EncodeUtf8(userDO.getPassword()));
        resultCount = userMapper.insert(userDO);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");

    }
}
