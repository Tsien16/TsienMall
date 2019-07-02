package com.tsien.mall.controller.protal;

import com.github.pagehelper.PageInfo;
import com.tsien.mall.constant.Const;
import com.tsien.mall.constant.ResponseCodeEnum;
import com.tsien.mall.model.ShippingDO;
import com.tsien.mall.model.UserDO;
import com.tsien.mall.service.ShippingService;
import com.tsien.mall.util.ServerResponse;
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
 * @date 2019/7/3 0003 1:39
 */

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Resource
    private ShippingService shippingService;

    /**
     * 查询所有收货地址
     *
     * @param session  session
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return 收货地址
     */
    @GetMapping("list.do")
    public ServerResponse<PageInfo> list(HttpSession session,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return shippingService.list(userDO.getId(), pageNum, pageSize);

    }

    /**
     * 新增地址
     *
     * @param session    session
     * @param shippingDO shippingDO
     * @return shippingId
     */
    @GetMapping("add.do")
    public ServerResponse add(HttpSession session, ShippingDO shippingDO) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return shippingService.insert(userDO.getId(), shippingDO);

    }

    /**
     * 删除地址
     *
     * @param session    session
     * @param shippingId shippingId
     * @return 删除的结果
     */
    @GetMapping("delete.do")
    public ServerResponse delete(HttpSession session, @RequestParam("shippingId") Integer shippingId) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return shippingService.delete(userDO.getId(), shippingId);

    }

    /**
     * 更新收获地址
     *
     * @param session    session
     * @param shippingDO shippingDO
     * @return 更新的结果
     */
    @GetMapping("update.do")
    public ServerResponse update(HttpSession session, ShippingDO shippingDO) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return shippingService.update(userDO.getId(), shippingDO);

    }

    /**
     * 查询地址详情
     *
     * @param session    session
     * @param shippingId shippingId
     * @return 地址详情
     */
    @GetMapping("detail.do")
    public ServerResponse<ShippingDO> detail(HttpSession session, Integer shippingId) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return shippingService.detail(userDO.getId(), shippingId);

    }
}
