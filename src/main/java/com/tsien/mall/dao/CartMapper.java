package com.tsien.mall.dao;

import com.tsien.mall.model.CartDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 查询购物车
     *
     * @param userId    userId
     * @param productId productId
     * @return cartDO
     */
    CartDO getCartByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    /**
     * 根据用户查询购物车列表
     *
     * @param userId userId
     * @return cartList
     */
    List<CartDO> listCartsByUserId(Integer userId);

    /**
     * 查询未勾选购物车的数量
     *
     * @param userId userId
     * @return 未勾选购物车的数量
     */
    int countCartOfUnCheckedByUserId(Integer userId);

    /**
     * 根据产品ID删除购物车
     *
     * @param userId        userId
     * @param productIdList productIdList
     * @return 删除的行数
     */
    int deleteByProductIds(@Param("userId") Integer userId, @Param("productIdList") List<String> productIdList);

    /**
     * 更新购物车选择状态
     *
     * @param userId    userId
     * @param productId productId
     * @param checked   checked
     * @return 影响的行数
     */
    int updateCartCheckedOrUnChecked(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("checked") Integer checked);

    /**
     * 统计购物车中的商品数量
     *
     * @param userId userId
     * @return 商品数量
     */
    int countCartProducts(Integer userId);
}