package com.tsien.mall.service;

import com.github.pagehelper.PageInfo;
import com.tsien.mall.util.ServerResponse;
import com.tsien.mall.vo.OrderVO;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/5 0005 1:09
 */

public interface OrderService {

    /**
     * 查询订单列表
     *
     * @param userId   userId
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return pageInfo
     */
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

    /**
     * 创建订单
     *
     * @param userId     userId
     * @param shippingId shippingId
     * @return orderVo
     */
    ServerResponse insert(Integer userId, Integer shippingId);

    /**
     * 取消订单
     *
     * @param userId  userId
     * @param orderNo orderNo
     * @return 取消的结果
     */
    ServerResponse<String> delete(Integer userId, Long orderNo);

    /**
     * 查询购物车商品
     *
     * @param userId userId
     * @return orderProductVO
     */
    ServerResponse getOrderCartProduct(Integer userId);

    /**
     * 查询订单详情
     *
     * @param userId  userId
     * @param orderNo orderNo
     * @return orderVo
     */
    ServerResponse<OrderVO> getOrderDetail(Integer userId, Long orderNo);

    /**
     * 管理员查询所有订单
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return 订单列表
     */
    ServerResponse<PageInfo> listOfAdmin(int pageNum, int pageSize);

    /**
     * 查看订单详情
     *
     * @param orderNo orderNo
     * @return 订单详情
     */
    ServerResponse<OrderVO> detail(Long orderNo);

    /**
     * 搜索订单
     *
     * @param orderNo  orderNo
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return 搜索结果
     */
    ServerResponse<PageInfo> search(Long orderNo, int pageNum, int pageSize);

    /**
     * 发货
     *
     * @param orderNo orderNo
     * @return 发货结果
     */
    ServerResponse<String> sendGoods(Long orderNo);
}
