package com.tsien.mall.dao;

import com.tsien.mall.model.OrderDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/22 0022 15:31
 */

public interface OrderMapper {

    /**
     * 通过主键删除订单
     *
     * @param id 订单ID
     * @return 删除的数量
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入订单数据
     *
     * @param record 订单实体
     * @return 插入的数量
     */
    int insert(OrderDO record);

    /**
     * 选择性的插入订单数据
     *
     * @param record 订单实体
     * @return 插入的数量
     */
    int insertSelective(OrderDO record);

    /**
     * 通过主键查询订单
     *
     * @param id 订单ID
     * @return 订单实体
     */
    OrderDO getByPrimaryKey(Integer id);

    /**
     * 通过主键选择性更新订单
     *
     * @param record 订单实体
     * @return 更新的数量
     */
    int updateByPrimaryKeySelective(OrderDO record);

    /**
     * 通过主键更新订单
     *
     * @param record 订单实体
     * @return 更新的数量
     */
    int updateByPrimaryKey(OrderDO record);

    /**
     * 查询订单
     *
     * @param userId  userId
     * @param orderNo orderNo
     * @return orderDo
     */
    OrderDO getOrderByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);

    /**
     * 根据userId查询订单
     *
     * @param userId userId
     * @return return
     */
    List<OrderDO> listOrdersByUserId(Integer userId);
}