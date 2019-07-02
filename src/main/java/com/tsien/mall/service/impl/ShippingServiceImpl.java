package com.tsien.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.tsien.mall.constant.ResponseCodeEnum;
import com.tsien.mall.dao.ShippingMapper;
import com.tsien.mall.model.ShippingDO;
import com.tsien.mall.service.ShippingService;
import com.tsien.mall.util.ServerResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/3 0003 1:43
 */

@Service
public class ShippingServiceImpl implements ShippingService {

    @Resource
    private ShippingMapper shippingMapper;

    /**
     * 查询所有收货地址
     *
     * @param userId   userId
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return pageInfo
     */
    @Override
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<ShippingDO> shippingList = shippingMapper.listShippings(userId);

        PageInfo pageInfo = new PageInfo<>(shippingList);

        return ServerResponse.createBySuccess(pageInfo);

    }

    /**
     * 新增地址
     *
     * @param userId     userId
     * @param shippingDO shippingDO
     * @return shippingId
     */
    @Override
    public ServerResponse insert(Integer userId, ShippingDO shippingDO) {

        shippingDO.setUserId(userId);
        int rowCount = shippingMapper.insert(shippingDO);

        if (rowCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId", shippingDO.getId());
            return ServerResponse.createBySuccess("新建地址成功", result);
        }

        return ServerResponse.createByErrorMessage("新建地址失败");

    }

    /**
     * 删除地址
     *
     * @param userId     userId
     * @param shippingId shippingId
     * @return 删除的结果
     */
    @Override
    public ServerResponse delete(Integer userId, Integer shippingId) {

        if (shippingId == null || userId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }

        int resultCount = shippingMapper.deleteByUserIdAndShippingId(userId, shippingId);

        if (resultCount > 0) {
            return ServerResponse.createBySuccess("删除地址成功");
        }

        return ServerResponse.createByErrorMessage("删除地址失败");

    }

    /**
     * 更新收货地址
     *
     * @param userId     userId
     * @param shippingDO shippingDO
     * @return 更新的结果
     */
    @Override
    public ServerResponse update(Integer userId, ShippingDO shippingDO) {

        shippingDO.setUserId(userId);
        int resultCount = shippingMapper.updateByUserId(shippingDO);

        if (resultCount > 0) {
            return ServerResponse.createBySuccess("更新地址成功");
        }

        return ServerResponse.createByErrorMessage("更新地址失败");

    }

    /**
     * 查询地址详情
     *
     * @param userId     userId
     * @param shippingId shippingId
     * @return 地址详情
     */
    @Override
    public ServerResponse<ShippingDO> detail(Integer userId, Integer shippingId) {

        ShippingDO shippingDO = shippingMapper.getByUserIdAndShippingId(userId, shippingId);

        if (shippingDO == null) {
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        }

        return ServerResponse.createBySuccess(shippingDO);

    }
}
