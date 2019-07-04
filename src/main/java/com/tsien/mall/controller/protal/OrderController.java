package com.tsien.mall.controller.protal;

import com.github.pagehelper.PageInfo;
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
import javax.servlet.http.HttpSession;

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


}
