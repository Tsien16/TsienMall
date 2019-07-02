package com.tsien.mall.service;

import com.github.pagehelper.PageInfo;
import com.tsien.mall.model.ShippingDO;
import com.tsien.mall.util.ServerResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/3 0003 1:42
 */

public interface ShippingService {

    /**
     * 查询所有收货地址
     *
     * @param userId   userId
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return pageInfo
     */
    ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

    /**
     * 新增地址
     *
     * @param userId     userId
     * @param shippingDO shippingDO
     * @return shippingId
     */
    ServerResponse insert(Integer userId, ShippingDO shippingDO);

    /**
     * 删除地址
     *
     * @param userId     userId
     * @param shippingId shippingId
     * @return 删除的结果
     */
    ServerResponse delete(Integer userId, Integer shippingId);

    /**
     * 更新收货地址
     *
     * @param userId     userId
     * @param shippingDO shippingDO
     * @return 更新的结果
     */
    ServerResponse update(Integer userId, ShippingDO shippingDO);

    /**
     * 查询地址详情
     *
     * @param userId     userId
     * @param shippingId shippingId
     * @return 地址详情
     */
    ServerResponse<ShippingDO> detail(Integer userId, Integer shippingId);
}
