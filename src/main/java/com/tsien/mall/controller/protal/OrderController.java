package com.tsien.mall.controller.protal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.tsien.mall.constant.AlipayConsts;
import com.tsien.mall.constant.Const;
import com.tsien.mall.constant.ResponseCodeEnum;
import com.tsien.mall.model.UserDO;
import com.tsien.mall.service.OrderService;
import com.tsien.mall.util.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/5 0005 1:01
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Resource
    private OrderService orderService;

    /**
     * 查询所有订单
     *
     * @param session  session
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return 订单列表
     */
    @GetMapping("list.do")
    public ServerResponse<PageInfo> list(HttpSession session,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return orderService.list(userDO.getId(), pageNum, pageSize);
    }

    /**
     * 创建订单
     *
     * @param session    session
     * @param shippingId shippingId
     * @return 订单详情
     */
    @GetMapping("create.do")
    public ServerResponse create(HttpSession session,
                                 @RequestParam("shippingId") Integer shippingId) {

        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return orderService.insert(userDO.getId(), shippingId);
    }

    /**
     * 取消订单
     *
     * @param session session
     * @param orderNo orderNo
     * @return 取消的结果
     */
    @GetMapping("cancel.do")
    public ServerResponse cancel(HttpSession session,
                                 @RequestParam("orderNo") Long orderNo) {

        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return orderService.delete(userDO.getId(), orderNo);
    }

    /**
     * 获取购物车商品
     *
     * @param session session
     * @return 购物车商品
     */
    @GetMapping("get_order_cart_product.do")
    public ServerResponse getOrderCartProduct(HttpSession session) {

        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return orderService.getOrderCartProduct(userDO.getId());
    }

    /**
     * 获取订单详情
     *
     * @param session session
     * @param orderNo orderNo
     * @return 订单详情
     */
    @GetMapping("detail.do")
    public ServerResponse detail(HttpSession session, @RequestParam("orderNo") Long orderNo) {

        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return orderService.getOrderDetail(userDO.getId(), orderNo);
    }

    /**
     * 订单支付
     *
     * @param session session
     * @param request request
     * @param orderNo orderNo
     * @return 订单支付结果
     */
    @GetMapping("pay.do")
    public ServerResponse pay(HttpSession session, HttpServletRequest request,
                              @RequestParam("orderNo") Long orderNo) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        String path = request.getSession().getServletContext().getRealPath("upload");

        return orderService.pay(userDO.getId(), orderNo, path);
    }

    /**
     * 支付宝回调
     *
     * @param request request
     * @return 支付结果
     */
    @GetMapping("alipay_callback.do")
    public Object alipayCallback(HttpServletRequest request) {

        Map<String, String> params = Maps.newHashMap();

        Map requestParameterMap = request.getParameterMap();
        for (Object key : requestParameterMap.keySet()) {
            String name = (String) key;
            String[] values = (String[]) requestParameterMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        logger.info("支付宝回调，sign:{},trade_status:{},参数:{}", params.get("sign"), params.get("trade_status"),
                params.toString());


        // 验证回调的正确性，是不是支付宝发的，还要避免重复通知
        params.remove("sign_type");
        try {
            boolean alipayRsaCheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8",
                    Configs.getSignType());

            if (!alipayRsaCheckedV2) {
                return ServerResponse.createByErrorMessage("非法请求，验证不通过，再恶意请求我就报警了");
            }

        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常", e);
        }

        // todo 各种参数验证

        ServerResponse response = orderService.alipayCallback(params);

        if (response.isSuccess()) {
            return AlipayConsts.RESPONSE_SUCCESS;
        }
        return AlipayConsts.RESPONSE_FAILED;
    }

    /**
     * 查询订单支付状态
     *
     * @param session session
     * @param orderNo orderNo
     * @return 订单状态
     */
    @GetMapping("query_order_pay_status.do")
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session,
                                                       @RequestParam("orderNo") Long orderNo) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        ServerResponse response = orderService.queryOrderPayStatus(userDO.getId(), orderNo);
        if (response.isSuccess()) {
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }


}
