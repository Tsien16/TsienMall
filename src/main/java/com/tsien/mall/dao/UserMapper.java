package com.tsien.mall.dao;

import com.tsien.mall.model.UserDO;

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
}