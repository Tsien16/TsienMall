package com.tsien.mall.controller.backend;

import com.tsien.mall.constant.Const;
import com.tsien.mall.constant.ResponseCodeEnum;
import com.tsien.mall.model.ProductDO;
import com.tsien.mall.model.UserDO;
import com.tsien.mall.service.ProductService;
import com.tsien.mall.service.UserService;
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
 * @date 2019/6/30 0030 11:51
 */

@RestController
@RequestMapping("manage/product")
public class ProductManageController {

    @Resource
    private UserService userService;

    @Resource
    private ProductService productService;

    /**
     * 新增或者更新商品
     *
     * @param session   session
     * @param productDO productDO
     * @return 保存的结果
     */
    @GetMapping("save.do")
    public ServerResponse saveProduct(HttpSession session, ProductDO productDO) {

        // 检查用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请使用管理员账号登陆");
        }

        // 检查用户是否是管理员,是管理员就更新
        if (userService.checkUserRoleOfAdmin(userDO).isSuccess()) {
            return productService.saveOrUpdateProduct(productDO);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，无权限操作");
        }
    }

    /**
     * 设置产品销售状态
     *
     * @param session   session
     * @param productId productId
     * @param status    status
     * @return 更新的结果
     */
    @GetMapping("set_product_sale_status.do")
    public ServerResponse setProductSaleStatus(HttpSession session,
                                               @RequestParam("productId") Integer productId,
                                               @RequestParam("status") Integer status) {

        // 检查用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请使用管理员账号登陆");
        }

        // 检查用户是否是管理员,是管理员就更新
        if (userService.checkUserRoleOfAdmin(userDO).isSuccess()) {
            return productService.updateProductSaleStatus(productId, status);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，无权限操作");
        }
    }

    /**
     * 查看商品详情
     *
     * @param session   session
     * @param productId productId
     * @return 商品详情
     */
    @GetMapping("detail.do")
    public ServerResponse detail(HttpSession session, @RequestParam("productId") Integer productId) {

        // 检查用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请使用管理员账号登陆");
        }

        // 检查用户是否是管理员,是管理员就更新
        if (userService.checkUserRoleOfAdmin(userDO).isSuccess()) {
            return productService.getProductDetailOfManage(productId);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，无权限操作");
        }
    }

    @GetMapping("list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        // 检查用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请使用管理员账号登陆");
        }

        // 检查用户是否是管理员,是管理员就更新
        if (userService.checkUserRoleOfAdmin(userDO).isSuccess()) {
            return productService.listProducts(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，无权限操作");
        }
    }


}
