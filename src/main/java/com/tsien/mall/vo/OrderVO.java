package com.tsien.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/5 0005 2:46
 */

@Data
public class OrderVO {

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 实际付款金额,单位是元,保留两位小数
     */
    private BigDecimal payment;

    /**
     * 支付类型,1-在线支付
     */
    private Integer paymentType;

    /**
     * 支付类型描述
     */
    private String paymentTypeDesc;

    /**
     * 运费,单位是元
     */
    private Integer postage;

    /**
     * 订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭
     */
    private Integer status;

    /**
     * 订单状态描述
     */
    private String statusDesc;

    /**
     * 支付时间
     */
    private String paymentTime;

    /**
     * 发货时间
     */
    private String sendTime;

    /**
     * 交易完成时间
     */
    private String endTime;

    /**
     * 交易关闭时间
     */
    private String closeTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 订单明细
     */
    private List<OrderItemVO> orderItemVoList;

    /**
     * 图片地址域名
     */
    private String imageHost;

    /**
     * 收货地址ID
     */
    private Integer shippingId;

    /**
     * 收货人名称
     */
    private String receiverName;

    /**
     * shippingVo
     */
    private ShippingVO shippingVO;
}
