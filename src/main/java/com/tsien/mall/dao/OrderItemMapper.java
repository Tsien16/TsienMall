package com.tsien.mall.dao;

import com.tsien.mall.model.OrderItemDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/22 0022 15:31
 */

public interface OrderItemMapper {

    /**
     * 通过主键删除订单详情
     *
     * @param id 订单详情ID
     * @return 删除的数量
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入订单详情数据
     *
     * @param record 订单详情实体
     * @return 插入的数量
     */
    int insert(OrderItemDO record);

    /**
     * 选择性的插入订单详情数据
     *
     * @param record 订单详情实体
     * @return 插入的数量
     */
    int insertSelective(OrderItemDO record);

    /**
     * 通过主键查询订单详情
     *
     * @param id 订单详情ID
     * @return 订单详情实体
     */
    OrderItemDO getByPrimaryKey(Integer id);

    /**
     * 通过主键选择性更新订单详情
     *
     * @param record 订单详情实体
     * @return 更新的数量
     */
    int updateByPrimaryKeySelective(OrderItemDO record);

    /**
     * 通过主键更新订单详情
     *
     * @param record 订单详情实体
     * @return 更新的数量
     */
    int updateByPrimaryKey(OrderItemDO record);

    /**
     * 批量插入
     *
     * @param orderItemList orderItemList
     */
    void insertBatch(@Param("orderItemList") List<OrderItemDO> orderItemList);

    /**
     * 查询子订单
     *
     * @param userId  userId
     * @param orderNo orderNo
     * @return orderItemList
     */
    List<OrderItemDO> listOrderItemsByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    /**
     * 查询orderItem
     *
     * @param orderNo orderNo
     * @return orderItemList
     */
    List<OrderItemDO> listOrderItemsByOrderNo(Long orderNo);
}