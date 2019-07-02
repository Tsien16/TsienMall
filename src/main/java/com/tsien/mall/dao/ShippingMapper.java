package com.tsien.mall.dao;

import com.tsien.mall.model.ShippingDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/22 0022 15:31
 */

public interface ShippingMapper {

    /**
     * 通过主键删除收货地址
     *
     * @param id 收货地址ID
     * @return 删除的数量
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入收货地址数据
     *
     * @param record 收货地址实体
     * @return 插入的数量
     */
    int insert(ShippingDO record);

    /**
     * 选择性的插入收货地址数据
     *
     * @param record 收货地址实体
     * @return 插入的数量
     */
    int insertSelective(ShippingDO record);

    /**
     * 通过主键查询收货地址
     *
     * @param id 收货地址ID
     * @return 收货地址实体
     */
    ShippingDO getByPrimaryKey(Integer id);

    /**
     * 通过主键选择性更新收货地址
     *
     * @param record 收货地址实体
     * @return 更新的数量
     */
    int updateByPrimaryKeySelective(ShippingDO record);

    /**
     * 通过主键更新收货地址
     *
     * @param record 收货地址实体
     * @return 更新的数量
     */
    int updateByPrimaryKey(ShippingDO record);

    /**
     * 根据用户ID和地址IP删除
     *
     * @param userId     userId
     * @param shippingId shippingId
     * @return 生效行数
     */
    int deleteByUserIdAndShippingId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    /**
     * 更新收货地址
     *
     * @param record record
     * @return 更新的结果
     */
    int updateByUserId(ShippingDO record);

    /**
     * 根据用户ID和地址IP查询
     *
     * @param userId     userId
     * @param shippingId shippingId
     * @return ShippingDO
     */
    ShippingDO getByUserIdAndShippingId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    /**
     * 查询所有收货地址
     *
     * @param userId userId
     * @return shippingList
     */
    List<ShippingDO> listShippings(Integer userId);
}