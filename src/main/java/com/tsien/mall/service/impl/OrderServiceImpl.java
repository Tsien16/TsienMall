package com.tsien.mall.service.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tsien.mall.constant.*;
import com.tsien.mall.dao.*;
import com.tsien.mall.model.*;
import com.tsien.mall.service.OrderService;
import com.tsien.mall.util.*;
import com.tsien.mall.vo.OrderItemVO;
import com.tsien.mall.vo.OrderProductVO;
import com.tsien.mall.vo.OrderVO;
import com.tsien.mall.vo.ShippingVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

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

    @Resource
    private PayInfoMapper payInfoMapper;

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
            orderItemDO.setTotalPrice(BigDecimalUtil.multiply(productDO.getPrice().doubleValue(),
                    cartItem.getQuantity()));

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
                orderItemList = orderItemMapper.listOrderItemsByOrderNo(order.getOrderNo());

            } else {
                orderItemList = orderItemMapper.listOrderItemsByUserIdAndOrderNo(userId,
                        order.getOrderNo());
            }
            OrderVO orderVO = this.assembleOrderVO(order, orderItemList);
            orderVoList.add(orderVO);
        }

        return orderVoList;

    }

    /**
     * 管理员查询所有订单
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return 订单列表
     */
    @Override
    public ServerResponse<PageInfo> listOfAdmin(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<OrderDO> orderList = orderMapper.listOrders();
        List<OrderVO> orderVoList = this.assembleOrderVoList(null, orderList);

        PageInfo pageInfo = new PageInfo<>(orderVoList);

        return ServerResponse.createBySuccess(pageInfo);

    }

    /**
     * 查看订单详情
     *
     * @param orderNo orderNo
     * @return 订单详情
     */
    @Override
    public ServerResponse<OrderVO> detail(Long orderNo) {

        OrderDO orderDO = orderMapper.getOrderByOrderNo(orderNo);
        if (orderDO != null) {
            List<OrderItemDO> orderItemList = orderItemMapper.listOrderItemsByOrderNo(orderNo);
            OrderVO orderVO = this.assembleOrderVO(orderDO, orderItemList);

            return ServerResponse.createBySuccess(orderVO);
        }

        return ServerResponse.createByErrorMessage("此订单不存在");

    }

    /**
     * 搜索订单
     *
     * @param orderNo  orderNo
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return 搜索结果
     */
    @Override
    public ServerResponse<PageInfo> search(Long orderNo, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        OrderDO orderDO = orderMapper.getOrderByOrderNo(orderNo);
        if (orderDO != null) {
            List<OrderItemDO> orderItemList = orderItemMapper.listOrderItemsByOrderNo(orderNo);
            OrderVO orderVO = this.assembleOrderVO(orderDO, orderItemList);

            PageInfo pageInfo = new PageInfo<>(Lists.newArrayList(orderVO));

            return ServerResponse.createBySuccess(pageInfo);
        }

        return ServerResponse.createByErrorMessage("此订单不存在");

    }

    /**
     * 发货
     *
     * @param orderNo orderNo
     * @return 发货结果
     */
    @Override
    public ServerResponse<String> sendGoods(Long orderNo) {

        OrderDO orderDO = orderMapper.getOrderByOrderNo(orderNo);

        if (orderDO != null) {
            if (orderDO.getStatus() == OrderStatusEnum.PAID.getCode()) {
                orderDO.setStatus(OrderStatusEnum.SHIPPED.getCode());
                orderDO.setSendTime(LocalDateTime.now());
                orderMapper.updateByPrimaryKeySelective(orderDO);
                return ServerResponse.createBySuccess("发货成功");
            }

            return ServerResponse.createByErrorMessage("发货失败");

        }

        return ServerResponse.createByErrorMessage("订单不存在");
    }

    /**
     * 订单支付
     *
     * @param userId  userId
     * @param orderNo orderNo
     * @param path    path
     * @return 支付结果
     */
    @Override
    public ServerResponse pay(Integer userId, Long orderNo, String path) {
        Map<String, String> resultMap = Maps.newHashMap();

        OrderDO orderDO = orderMapper.getOrderByUserIdAndOrderNo(userId, orderNo);
        if (orderDO == null) {
            return ServerResponse.createByErrorMessage("此用户订单不存在");
        }

        resultMap.put("orderNo", String.valueOf(orderDO.getOrderNo()));

        /*
         * (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
         * 需保证商户系统端不能重复，建议通过数据库sequence生成，
         */
        String outTradeNo = orderDO.getOrderNo().toString();

        /*
         * (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
         */
        String subject = "TsienMall扫码支付，订单号：" + outTradeNo;

        /*
         * (必填) 订单总金额，单位为元，不能超过1亿元
         * 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
         */
        String totalAmount = orderDO.getPayment().toString();

        /*
         * (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
         * 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
         */
        String undiscountableAmount = "0";

        /*
         * 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
         * 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appId对应的PID
         */
        String sellerId = "";

        /*
         * 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
         */
        String body = "订单：" + outTradeNo + ",购买商品共:" + totalAmount + "元";

        /*
         * 商户操作员编号，添加此参数可以为商户操作员做销售统计
         */
        String operatorId = "test_operator_id";

        /*
         * (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
         */
        String storeId = "test_store_id";

        /*
         * 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
         */
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        /*
         * 支付超时，定义为120分钟
         */
        String timeoutExpress = "120m";

        /*
         * 商品明细列表，需填写购买商品详细信息，
         * 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
         */
        List<GoodsDetail> goodsDetailList = new ArrayList<>();
        List<OrderItemDO> orderItemList = orderItemMapper.listOrderItemsByUserIdAndOrderNo(userId, orderNo);
        for (OrderItemDO orderItem : orderItemList) {
            GoodsDetail goodsDetail = GoodsDetail.newInstance(orderItem.getProductId().toString(),
                    orderItem.getProductName(),
                    BigDecimalUtil.multiply(orderItem.getCurrentUnitPrice().doubleValue(), 100d).longValue(),
                    orderItem.getQuantity());
            goodsDetailList.add(goodsDetail);
        }

        /*
         * 创建扫码支付请求builder，设置请求参数
         */
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))
                .setGoodsDetailList(goodsDetailList);

        /*
         *  一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的alipayInfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("alipayInfo.properties");

        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                this.dumpResponse(response);

                File folder = new File(path);
                if (!folder.exists()) {
                    boolean setResult = folder.setWritable(true);
                    if (setResult) {
                        logger.info("设置文件夹可写权限成功");
                    } else {
                        logger.info("设置文件夹可写权限失败");
                    }

                    boolean makeResult = folder.mkdirs();
                    if (makeResult) {
                        logger.info("创建文件夹成功");
                    } else {
                        logger.info("创建文件夹失败");
                    }
                }
                // 需要修改为运行机器上的路径
                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                logger.info("filePath:" + qrPath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                File targetFile = new File(path, qrFileName);
                try {
                    FTPUtil.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    logger.error("上传二维码异常", e);
                }
                logger.info("qrPath:" + qrPath);
                String qrUrl = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFile.getName();
                resultMap.put("qrUrl", qrUrl);
                return ServerResponse.createBySuccess(resultMap);
            case FAILED:
                logger.error("支付宝预下单失败!!!");
                return ServerResponse.createByErrorMessage("支付宝预下单失败!!!");
            case UNKNOWN:
                logger.error("系统异常，预下单状态未知!!!");
                return ServerResponse.createByErrorMessage("系统异常，预下单状态未知!!!");
            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                return ServerResponse.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }

    }

    /**
     * 打印应答
     *
     * @param response response
     */
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }

    /**
     * 支付宝回调
     *
     * @param params params
     * @return 更新结果
     */
    @Override
    public ServerResponse alipayCallback(Map<String, String> params) {

        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");

        OrderDO orderDO = orderMapper.getOrderByOrderNo(orderNo);
        if (orderDO == null) {
            return ServerResponse.createByErrorMessage("非TsienMall商城的订单，回调忽略");
        }

        if (orderDO.getStatus() >= OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.createBySuccess("支付宝重复调用");
        }

        if (AlipayConstants.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            orderDO.setPaymentTime(DateTimeUtil.stringToDateTime(params.get("gmt_payment")));
            orderDO.setStatus(OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(orderDO);
        }

        PayInfoDO payInfoDO = new PayInfoDO();
        payInfoDO.setUserId(orderDO.getUserId());
        payInfoDO.setOrderNo(orderDO.getOrderNo());
        payInfoDO.setPayPlatform(PayPlatformEnum.ALIPAY.getCode());
        payInfoDO.setPlatformNumber(tradeNo);
        payInfoDO.setPlatformStatus(tradeStatus);

        payInfoMapper.insert(payInfoDO);

        return ServerResponse.createBySuccess();

    }

    /**
     * 查询订单支付状态
     *
     * @param userId  userId
     * @param orderNo orderNo
     * @return 支付状态
     */
    @Override
    public ServerResponse queryOrderPayStatus(Integer userId, Long orderNo) {

        OrderDO orderDO = orderMapper.getOrderByUserIdAndOrderNo(userId, orderNo);
        if (orderDO == null) {
            return ServerResponse.createByErrorMessage("用户没有该订单");
        }

        if (orderDO.getStatus() >= OrderStatusEnum.PAID.getCode()) {
            return ServerResponse.createBySuccess();
        }

        return ServerResponse.createByError();
    }


}
