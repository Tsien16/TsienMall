package com.tsien.mall.dao;

import com.tsien.mall.model.CartDO;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/22 0022 15:31
 */

public interface CartMapper {

    /**
     * 通过主键删除购物车
     *
     * @param id 购物车ID
     * @return 删除的数量
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入购物车数据
     *
     * @param record 购物车实体
     * @return 插入的数量
     */
    int insert(CartDO record);

    /**
     * 选择性的插入购物车数据
     *
     * @param record 购物车实体
     * @return 插入的数量
     */
    int insertSelective(CartDO record);

    /**
     * 通过主键查询购物车
     *
     * @param id 购物车ID
     * @return 购物车实体
     */
    CartDO getByPrimaryKey(Integer id);

    /**
     * 通过主键选择性更新购物车
     *
     * @param record 购物车实体
     * @return 更新的数量
     */
    int updateByPrimaryKeySelective(CartDO record);

    /**
     * 通过主键更新购物车
     *
     * @param record 购物车实体
     * @return 更新的数量
     */
    int updateByPrimaryKey(CartDO record);
}