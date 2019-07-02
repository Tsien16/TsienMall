package com.tsien.mall.controller.protal;

import com.tsien.mall.constant.CartStatusEnum;
import com.tsien.mall.constant.Const;
import com.tsien.mall.constant.ResponseCodeEnum;
import com.tsien.mall.model.UserDO;
import com.tsien.mall.service.CartService;
import com.tsien.mall.util.ServerResponse;
import com.tsien.mall.vo.CartVO;
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
 * @date 2019/7/2 0002 16:04
 */

@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @GetMapping("list.do")
    public ServerResponse<CartVO> list(HttpSession session) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return cartService.list(userDO.getId());
    }

    /**
     * 增加购物车
     *
     * @param session   session
     * @param productId productId
     * @param quantity  quantity
     * @return 增加的结果
     */
    @GetMapping("add.do")
    public ServerResponse<CartVO> add(HttpSession session,
                                      @RequestParam("productId") Integer productId,
                                      @RequestParam("quantity") Integer quantity) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return cartService.insertOrUpdate(userDO.getId(), productId, quantity);
    }

    /**
     * 更新购物车
     *
     * @param session   session
     * @param productId productId
     * @param quantity  quantity
     * @return 更新的结果
     */
    @GetMapping("update.do")
    public ServerResponse<CartVO> update(HttpSession session,
                                         @RequestParam("productId") Integer productId,
                                         @RequestParam("quantity") Integer quantity) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return cartService.update(userDO.getId(), productId, quantity);
    }

    /**
     * 删除购物车
     *
     * @param session    session
     * @param productIds productIds
     * @return cartVO
     */
    @GetMapping("delete.do")
    public ServerResponse<CartVO> delete(HttpSession session,
                                         @RequestParam("productIds") String productIds) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return cartService.delete(userDO.getId(), productIds);
    }

    /**
     * 购物车全选
     *
     * @param session session
     * @return CartVO
     */
    @GetMapping("select_all.do")
    public ServerResponse<CartVO> selectAll(HttpSession session) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return cartService.updateCartCheckedOrUnChecked(userDO.getId(), null, CartStatusEnum.CHECKED.getCode());
    }

    /**
     * 购物车全不选
     *
     * @param session session
     * @return CartVO
     */
    @GetMapping("un_select_all.do")
    public ServerResponse<CartVO> unSelectAll(HttpSession session) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return cartService.updateCartCheckedOrUnChecked(userDO.getId(), null, CartStatusEnum.UN_CHECKED.getCode());
    }

    /**
     * 选择购物车
     *
     * @param session   session
     * @param productId productId
     * @return CartVO
     */
    @GetMapping("select.do")
    public ServerResponse<CartVO> select(HttpSession session, @RequestParam("productId") Integer productId) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return cartService.updateCartCheckedOrUnChecked(userDO.getId(), productId, CartStatusEnum.CHECKED.getCode());
    }

    /**
     * 取消选择购物车
     *
     * @param session   session
     * @param productId productId
     * @return CartVO
     */
    @GetMapping("un_select.do")
    public ServerResponse<CartVO> unSelect(HttpSession session, @RequestParam("productId") Integer productId) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),
                    ResponseCodeEnum.NEED_LOGIN.getDesc());
        }

        return cartService.updateCartCheckedOrUnChecked(userDO.getId(), productId, CartStatusEnum.UN_CHECKED.getCode());
    }

    /**
     * 统计购物车商品数量
     *
     * @param session session
     * @return 购物车商品数量
     */
    @GetMapping("get_cart_product_count.do")
    public ServerResponse<Integer> getCartProductCount(HttpSession session) {

        // 判断是否登陆，如果未登录强制登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createBySuccess(0);
        }

        return cartService.countCartProducts(userDO.getId());
    }


}
