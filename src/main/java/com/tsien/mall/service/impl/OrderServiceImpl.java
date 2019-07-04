package com.tsien.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tsien.mall.constant.OrderStatusEnum;
import com.tsien.mall.constant.PaymentTypeEnum;
import com.tsien.mall.constant.ProductStatusEnum;
import com.tsien.mall.dao.*;
import com.tsien.mall.model.*;
import com.tsien.mall.service.OrderService;
import com.tsien.mall.util.BigDecimalUtil;
import com.tsien.mall.util.DateTimeUtil;
import com.tsien.mall.util.PropertiesUtil;
import com.tsien.mall.util.ServerResponse;
import com.tsien.mall.vo.OrderItemVO;
import com.tsien.mall.vo.OrderProductVO;
import com.tsien.mall.vo.OrderVO;
import com.tsien.mall.vo.ShippingVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/5 0005 1:09
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private CartMapper cartMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private ShippingMapper shippingMapper;


    /**
     * 查询订单列表
     *
     * @param userId   userId
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return pageInfo
     */
    @Override
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<OrderDO> orderList = orderMapper.listOrdersByUserId(userId);
        List<OrderVO> orderVoList = this.assembleOrderVoList(userId, orderList);

        PageInfo pageInfo = new PageInfo<>(orderVoList);

        return ServerResponse.createBySuccess(pageInfo);

    }


    /**
     * 创建订单
     *
     * @param userId     userId
     * @param shippingId shippingId
     * @return orderVo
     */
    @Override
    public ServerResponse insert(Integer userId, Integer shippingId) {

        // 从购物车中获取已勾选的商品
        List<CartDO> cartList = cartMapper.listCartsOfCheckedByUserId(userId);

        // 计算这个订单的总价
        ServerResponse response = this.getCartOrderItem(userId, cartList);

        if (!response.isSuccess()) {
            return response;
        }

        List<OrderItemDO> orderItemList = (List<OrderItemDO>) response.getData();

        BigDecimal payment = this.getOrderTotalPrice(orderItemList);

        // 生成订单
        OrderDO orderDO = this.assembleOrder(userId, shippingId, payment);
        if (orderDO == null) {
            return ServerResponse.createByErrorMessage("生成订单错误");
        }

        if (CollectionUtils.isEmpty(orderItemList)) {
            return ServerResponse.createByErrorMessage("购物车为空");
        }

        for (OrderItemDO orderItem : orderItemList) {
            orderItem.setOrderNo(orderDO.getOrderNo());
        }

        // mybatis批量插入
        orderItemMapper.insertBatch(orderItemList);

        // 生成成功，减少产品的库存
        this.reduceProductStock(orderItemList);

        //清空购物车
        this.cleanCart(cartList);

        // 返回给前端明细
        OrderVO orderVO = this.assembleOrderVO(orderDO, orderItemList);

        return ServerResponse.createBySuccess(orderVO);

    }

    /**
     * 取消订单
     *
     * @param userId  userId
     * @param orderNo orderNo
     * @return 取消的结果
     */
    @Override
    public ServerResponse<String> delete(Integer userId, Long orderNo) {

        OrderDO orderDO = orderMapper.getOrderByUserIdAndOrderNo(userId, orderNo);
        if (orderDO == null) {
            return ServerResponse.createByErrorMessage("该用户此订单不存在");
        }
        if (orderDO.getStatus() != OrderStatusEnum.NO_PAY.getCode()) {
            return ServerResponse.createByErrorMessage("已付款，无法取消订单");
        }

        OrderDO updateOrder = new OrderDO();
        updateOrder.setId(orderDO.getId());
        updateOrder.setStatus(OrderStatusEnum.CANCELED.getCode());

        int rowCount = orderMapper.updateByPrimaryKeySelective(updateOrder);

        if (rowCount > 0) {
            return ServerResponse.createBySuccess();
        }

        return ServerResponse.createByError();

    }

    /**
     * 查询购物车商品
     *
     * @param userId userId
     * @return orderProductVO
     */
    @Override
    public ServerResponse getOrderCartProduct(Integer userId) {

        OrderProductVO orderProductVO = new OrderProductVO();

        // 从购物车中获取商品
        List<CartDO> cartList = cartMapper.listCartsOfCheckedByUserId(userId);
        ServerResponse response = this.getCartOrderItem(userId, cartList);

        if (!response.isSuccess()) {
            return response;
        }

        List<OrderItemDO> orderItemList = (List<OrderItemDO>) response.getData();

        List<OrderItemVO> orderItemVoList = Lists.newArrayList();

        BigDecimal payment = new BigDecimal("0");

        for (OrderItemDO orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVoList.add(this.assembleOrderItemVO(orderItem));
        }

        orderProductVO.setOrderItemVoList(orderItemVoList);
        orderProductVO.setProductTotalPrice(payment);
        orderProductVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return ServerResponse.createBySuccess(orderProductVO);

    }

    /**
     * 查询订单详情
     *
     * @param userId  userId
     * @param orderNo orderNo
     * @return orderVo
     */
    @Override
    public ServerResponse<OrderVO> getOrderDetail(Integer userId, Long orderNo) {

        OrderDO orderDO = orderMapper.getOrderByUserIdAndOrderNo(userId, orderNo);

        if (orderDO != null) {
            List<OrderItemDO> orderItemList = orderItemMapper.listOrderItemsByUserIdAndOrderNo(userId, orderNo);
            OrderVO orderVO = this.assembleOrderVO(orderDO, orderItemList);
            return ServerResponse.createBySuccess(orderVO);
        }

        return ServerResponse.createByErrorMessage("没有找到该订单");

    }


    /**
     * 查询订单详情
     *
     * @param userId   userId
     * @param cartList cartList
     * @return orderItemList
     */
    private ServerResponse<List<OrderItemDO>> getCartOrderItem(Integer userId, List<CartDO> cartList) {

        List<OrderItemDO> orderItemList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(cartList)) {
            return ServerResponse.createByErrorMessage("购物车为空");
        }

        // 校验购物车的数据，包括产品的状态和数量
        for (CartDO cartItem : cartList) {
            OrderItemDO orderItemDO = new OrderItemDO();
            ProductDO productDO = productMapper.getByPrimaryKey(cartItem.getProductId());
            if (ProductStatusEnum.ON_SALE.getCode() != productDO.getStatus()) {
                return ServerResponse.createByErrorMessage("产品：" + productDO.getName() + "不是在线售卖状态");
            }

            // 校验产品库存
            if (cartItem.getQuantity() > productDO.getStock()) {
                return ServerResponse.createByErrorMessage("产品：" + productDO.getName() + "库存不足");
            }

            orderItemDO.setUserId(userId);
            orderItemDO.setProductId(productDO.getId());
            orderItemDO.setProductName(productDO.getName());
            orderItemDO.setProductImage(productDO.getMainImage());
            orderItemDO.setCurrentUnitPrice(productDO.getPrice());
            orderItemDO.setQuantity(cartItem.getQuantity());
            orderItemDO.setTotalPrice(BigDecimalUtil.multiply(productDO.getPrice().doubleValue(), cartItem.getQuantity()));

            orderItemList.add(orderItemDO);
        }

        return ServerResponse.createBySuccess(orderItemList);

    }

    /**
     * 计算订单总价
     *
     * @param orderItemList orderItemList
     * @return 订单总价
     */
    private BigDecimal getOrderTotalPrice(List<OrderItemDO> orderItemList) {
        BigDecimal payment = new BigDecimal("0");
        for (OrderItemDO orderItem : orderItemList) {
            payment = BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }

        return payment;

    }

    /**
     * 组装订单
     *
     * @param userId     userId
     * @param shippingId shippingId
     * @param payment    payment
     * @return 订单
     */
    private OrderDO assembleOrder(Integer userId, Integer shippingId, BigDecimal payment) {
        OrderDO orderDO = new OrderDO();
        orderDO.setOrderNo(this.generateOrderNo());
        orderDO.setUserId(userId);
        orderDO.setShippingId(shippingId);
        orderDO.setPayment(payment);
        orderDO.setPaymentType(PaymentTypeEnum.ONLINE_PAY.getCode());
        orderDO.setPostage(0);
        orderDO.setStatus(OrderStatusEnum.NO_PAY.getCode());

        return orderDO;

    }

    /**
     * 生成订单号
     *
     * @return 生成的订单号
     */
    private long generateOrderNo() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    /**
     * 减少库存
     *
     * @param orderItemList orderItemList
     */
    private void reduceProductStock(List<OrderItemDO> orderItemList) {

        for (OrderItemDO orderItem : orderItemList) {
            ProductDO productDO = productMapper.getByPrimaryKey(orderItem.getProductId());
            productDO.setStock(productDO.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKeySelective(productDO);
        }
    }

    /**
     * 清空购物车
     *
     * @param cartList cartList
     */
    private void cleanCart(List<CartDO> cartList) {
        for (CartDO cartItem : cartList) {
            cartMapper.deleteByPrimaryKey(cartItem.getId());
        }
    }

    /**
     * 组装OrderVO
     *
     * @param orderDO       orderDO
     * @param orderItemList orderItemList
     * @return orderVO
     */
    private OrderVO assembleOrderVO(OrderDO orderDO, List<OrderItemDO> orderItemList) {

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderNo(orderDO.getOrderNo());
        orderVO.setPayment(orderDO.getPayment());
        orderVO.setPaymentType(orderDO.getPaymentType());
        orderVO.setPaymentTypeDesc(PaymentTypeEnum.codeOf(orderDO.getPaymentType()).getValue());
        orderVO.setPostage(orderDO.getPostage());
        orderVO.setStatus(orderDO.getStatus());
        orderVO.setStatusDesc(OrderStatusEnum.codeOf(orderDO.getStatus()).getValue());
        orderVO.setPaymentTime(DateTimeUtil.dateTimeToString(orderDO.getPaymentTime()));
        orderVO.setSendTime(DateTimeUtil.dateTimeToString(orderDO.getSendTime()));
        orderVO.setEndTime(DateTimeUtil.dateTimeToString(orderDO.getEndTime()));
        orderVO.setCloseTime(DateTimeUtil.dateTimeToString(orderDO.getCloseTime()));
        orderVO.setCreateTime(DateTimeUtil.dateTimeToString(orderDO.getCreateTime()));
        orderVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        orderVO.setShippingId(orderDO.getShippingId());

        ShippingDO shippingDO = shippingMapper.getByPrimaryKey(orderDO.getShippingId());
        if (shippingDO != null) {
            orderVO.setReceiverName(shippingDO.getReceiverName());
            orderVO.setShippingVO(this.assembleShippingVO(shippingDO));
        }

        List<OrderItemVO> orderItemVoList = Lists.newArrayList();
        for (OrderItemDO orderItemDO : orderItemList) {
            orderItemVoList.add(this.assembleOrderItemVO(orderItemDO));
        }
        orderVO.setOrderItemVoList(orderItemVoList);

        return orderVO;
    }

    /**
     * 组装OrderItemVO
     *
     * @param orderItemDO orderItemDO
     * @return OrderItemVO
     */
    private OrderItemVO assembleOrderItemVO(OrderItemDO orderItemDO) {

        OrderItemVO orderItemVO = new OrderItemVO();
        orderItemVO.setOrderNo(orderItemDO.getOrderNo());
        orderItemVO.setProductId(orderItemDO.getProductId());
        orderItemVO.setProductName(orderItemDO.getProductName());
        orderItemVO.setProductImage(orderItemDO.getProductImage());
        orderItemVO.setCurrentUnitPrice(orderItemDO.getCurrentUnitPrice());
        orderItemVO.setQuantity(orderItemDO.getQuantity());
        orderItemVO.setTotalPrice(orderItemDO.getTotalPrice());
        orderItemVO.setCreateTime(DateTimeUtil.dateTimeToString(orderItemDO.getCreateTime()));

        return orderItemVO;

    }

    /**
     * 组装ShippingVO
     *
     * @param shippingDO shippingDO
     * @return shippingVO
     */
    private ShippingVO assembleShippingVO(ShippingDO shippingDO) {
        ShippingVO shippingVO = new ShippingVO();
        shippingVO.setReceiverName(shippingDO.getReceiverName());
        shippingVO.setReceiverPhone(shippingDO.getReceiverPhone());
        shippingVO.setReceiverMobile(shippingDO.getReceiverMobile());
        shippingVO.setReceiverProvince(shippingDO.getReceiverProvince());
        shippingVO.setReceiverCity(shippingDO.getReceiverCity());
        shippingVO.setReceiverDistrict(shippingDO.getReceiverDistrict());
        shippingVO.setReceiverAddress(shippingDO.getReceiverAddress());
        shippingVO.setReceiverZip(shippingDO.getReceiverZip());

        return shippingVO;

    }

    /**
     * 组装orderV0List
     *
     * @param userId    userId
     * @param orderList orderList
     * @return orderV0List
     */
    private List<OrderVO> assembleOrderVoList(Integer userId, List<OrderDO> orderList) {

        List<OrderVO> orderVoList = Lists.newArrayList();
        List<OrderItemDO> orderItemList;
        for (OrderDO order : orderList) {
            if (userId == null) {
                //TODO 管理员查询的时候不需要userId
                return null;

            } else {
                orderItemList = orderItemMapper.listOrderItemsByUserIdAndOrderNo(userId,
                        order.getOrderNo());
            }
            OrderVO orderVO = this.assembleOrderVO(order, orderItemList);
            orderVoList.add(orderVO);
        }

        return orderVoList;

    }
}
