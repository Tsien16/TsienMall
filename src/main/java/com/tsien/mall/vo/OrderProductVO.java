package com.tsien.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/5 0005 3:48
 */

@Data
public class OrderProductVO {

    /**
     * 订单详情列表
     */
    private List<OrderItemVO> orderItemVoList;

    /**
     * 产品总价
     */
    private BigDecimal productTotalPrice;

    /**
     * 图片地址
     */
    private String imageHost;

}
