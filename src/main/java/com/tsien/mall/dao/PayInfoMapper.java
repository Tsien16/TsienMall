package com.tsien.mall.dao;

import com.tsien.mall.model.PayInfoDO;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/22 0022 15:31
 */

public interface PayInfoMapper {

    /**
     * 通过主键删除支付信息
     *
     * @param id 支付信息ID
     * @return 删除的数量
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入支付信息数据
     *
     * @param record 支付信息实体
     * @return 插入的数量
     */
    int insert(PayInfoDO record);

    /**
     * 选择性的插入支付信息数据
     *
     * @param record 支付信息实体
     * @return 插入的数量
     */
    int insertSelective(PayInfoDO record);

    /**
     * 通过主键查询支付信息
     *
     * @param id 支付信息ID
     * @return 支付信息实体
     */
    PayInfoDO getByPrimaryKey(Integer id);

    /**
     * 通过主键选择性更新支付信息
     *
     * @param record 支付信息实体
     * @return 更新的数量
     */
    int updateByPrimaryKeySelective(PayInfoDO record);

    /**
     * 通过主键更新支付信息
     *
     * @param record 支付信息实体
     * @return 更新的数量
     */
    int updateByPrimaryKey(PayInfoDO record);
}