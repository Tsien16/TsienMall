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

    /**
     * 根据用户名获取忘记密码提示的问题
     *
     * @param username username
     * @return 忘记密码提示的问题
     */
    String getQuestionByUsername(String username);

    /**
     * 通过用户名、问题、答案查询用户数量
     *
     * @param username username
     * @param question question
     * @param answer   answer
     * @return 结果的数量
     */
    int countUsersByUsernameAndQuestionAndAnswer(@Param("username") String username, @Param("question") String question,
                                                 @Param("answer") String answer);


    /**
     * 通过用户名更新密码
     *
     * @param username username
     * @param password password
     * @return 更新的行数
     */
    int updatePasswordByUsername(@Param("username") String username, @Param("password") String password);

    /**
     * 根据userId和password查找用户
     *
     * @param userId   userId
     * @param password password
     * @return 匹配的行数
     */
    int countUsersByUserIdAndPassword(@Param("userId") Integer userId, @Param("password") String password);

    /**
     * 检查不是某userId下的email数量
     *
     * @param userId userId
     * @param email  email
     * @return 校验结果
     */
    int countUsersByUserIdAndEmail(@Param("userId") Integer userId, @Param("email") String email);

}