package com.tsien.mall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.tsien.mall.constant.Const;
import com.tsien.mall.constant.ResponseCodeEnum;
import com.tsien.mall.model.UserDO;
import com.tsien.mall.service.OrderService;
import com.tsien.mall.service.UserService;
import com.tsien.mall.util.ServerResponse;
import com.tsien.mall.vo.OrderVO;
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
 * @date 2019/7/5 0005 4:39
 */

@RestController
@RequestMapping("/manage/order")
public class OrderManageController {

    @Resource
    private UserService userService;

    @Resource
    private OrderService orderService;

    /**
     * 管理员查询所有订单
     *
     * @param session  session
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return 订单
     */
    @GetMapping("list.do")
    public ServerResponse<PageInfo> list(HttpSession session,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        // 检查用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请使用管理员账号登陆");
        }

        // 检查用户是否是管理员,是管理员就更新
        if (userService.checkUserRoleOfAdmin(userDO).isSuccess()) {
            return orderService.listOfAdmin(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，无权限操作");
        }

    }

    /**
     * 查看订单详情
     *
     * @param session session
     * @param orderNo orderNo
     * @return 订单详情
     */
    @GetMapping("detail.do")
    public ServerResponse<OrderVO> detail(HttpSession session,
                                          @RequestParam("orderNo") Long orderNo) {

        // 检查用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请使用管理员账号登陆");
        }

        // 检查用户是否是管理员,是管理员就更新
        if (userService.checkUserRoleOfAdmin(userDO).isSuccess()) {
            return orderService.detail(orderNo);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，无权限操作");
        }

    }

    /**
     * 搜索商品
     *
     * @param session  session
     * @param orderNo  orderNo
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return 搜索结果
     */
    @GetMapping("search.do")
    public ServerResponse<PageInfo> search(HttpSession session,
                                           @RequestParam("orderNo") Long orderNo,
                                           @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        // 检查用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请使用管理员账号登陆");
        }

        // 检查用户是否是管理员,是管理员就更新
        if (userService.checkUserRoleOfAdmin(userDO).isSuccess()) {
            return orderService.search(orderNo, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，无权限操作");
        }

    }

    /**
     * 发货
     *
     * @param session session
     * @param orderNo orderNo
     * @return 发货结果
     */
    @GetMapping("send_goods.do")
    public ServerResponse<String> sendGoods(HttpSession session,
                                            @RequestParam("orderNo") Long orderNo) {

        // 检查用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请使用管理员账号登陆");
        }

        // 检查用户是否是管理员,是管理员就更新
        if (userService.checkUserRoleOfAdmin(userDO).isSuccess()) {
            return orderService.sendGoods(orderNo);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，无权限操作");
        }

    }
}
