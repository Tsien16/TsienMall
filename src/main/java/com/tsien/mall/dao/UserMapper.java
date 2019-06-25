package com.tsien.mall.dao;

import com.tsien.mall.model.UserDO;
import org.apache.ibatis.annotations.Param;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/22 0022 15:31
 */

public interface UserMapper {

    /**
     * 通过主键删除用户
     *
     * @param id 用户ID
     * @return 删除的数量
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入用户数据
     *
     * @param record 用户实体
     * @return 插入的数量
     */
    int insert(UserDO record);

    /**
     * 选择性的插入用户数据
     *
     * @param record 用户实体
     * @return 插入的数量
     */
    int insertSelective(UserDO record);

    /**
     * 通过主键查询用户
     *
     * @param id 用户ID
     * @return 用户实体
     */
    UserDO getByPrimaryKey(Integer id);

    /**
     * 通过主键选择性更新用户
     *
     * @param record 用户实体
     * @return 更新的数量
     */
    int updateByPrimaryKeySelective(UserDO record);

    /**
     * 通过主键更新用户
     *
     * @param record 用户实体
     * @return 更新的数量
     */
    int updateByPrimaryKey(UserDO record);

    /**
     * 通过用户统计用户数量
     *
     * @param username 用户名
     * @return 该用户名的数量
     */
    int countUsersByUsername(String username);

    /**
     * 通过邮箱统计用户数量
     *
     * @param email 邮箱
     * @return 该邮箱的数量
     */
    int countUsersByEmail(String email);

    /**
     * 根据用户名密码查询用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 用户
     */
    UserDO getUserByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}