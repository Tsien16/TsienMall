package com.tsien.mall.service;

import com.tsien.mall.util.ServerResponse;
import com.tsien.mall.vo.CartVO;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/2 0002 16:16
 */

public interface CartService {

    /**
     * 查询购物车
     *
     * @param userId userId
     * @return cartVO
     */
    ServerResponse<CartVO> list(Integer userId);

    /**
     * 新增或者更新购物车
     *
     * @param userId    userId
     * @param productId productId
     * @param quantity  quantity
     * @return cartVO
     */
    ServerResponse<CartVO> insertOrUpdate(Integer userId, Integer productId, Integer quantity);

    /**
     * 更新购物车
     *
     * @param userId    userId
     * @param productId productId
     * @param quantity  quantity
     * @return CartVO
     */
    ServerResponse<CartVO> update(Integer userId, Integer productId, Integer quantity);

    /**
     * 删除购物车
     *
     * @param userId     userId
     * @param productIds productIds
     * @return 购物车
     */
    ServerResponse<CartVO> delete(Integer userId, String productIds);

    /**
     * 设置全选或者全不选
     *
     * @param userId    userId
     * @param productId productId
     * @param checked   checked
     * @return CartVO
     */
    ServerResponse<CartVO> updateCartCheckedOrUnChecked(Integer userId, Integer productId, Integer checked);

    /**
     * 统计购物车商品数量
     *
     * @param userId userId
     * @return 商品数量
     */
    ServerResponse<Integer> countCartProducts(Integer userId);


}
