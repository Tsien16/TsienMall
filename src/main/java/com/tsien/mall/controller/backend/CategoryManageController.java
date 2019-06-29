package com.tsien.mall.controller.backend;

import com.tsien.mall.constant.Const;
import com.tsien.mall.constant.ResponseCodeEnum;
import com.tsien.mall.model.UserDO;
import com.tsien.mall.service.CategoryService;
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
 * @date 2019/6/29 0029 11:41
 */

@RestController
@RequestMapping("manage/category")
public class CategoryManageController {

    @Resource
    private UserService userService;

    @Resource
    private CategoryService categoryService;

    /**
     * 增加分类
     *
     * @param session      session
     * @param categoryName categoryName
     * @param parentId     parentId
     * @return 增加品类的结果
     */
    @GetMapping("add_category.do")
    public ServerResponse addCategory(HttpSession session,
                                      @RequestParam("categoryName") String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0") int parentId) {

        // 校验用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }

        // 校验是否是管理员
        ServerResponse response = userService.checkUserRoleOfAdmin(userDO);
        if (response.isSuccess()) {
            return categoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }

    }

    /**
     * 设置商品分类名称
     *
     * @param session      session
     * @param categoryId   categoryId
     * @param categoryName categoryName
     * @return 设置结果
     */
    @GetMapping("set_category_name.do")
    public ServerResponse setCategoryName(HttpSession session,
                                          @RequestParam("categoryId") int categoryId,
                                          @RequestParam("categoryName") String categoryName) {

        // 校验用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }

        // 校验是否是管理员
        ServerResponse response = userService.checkUserRoleOfAdmin(userDO);
        if (response.isSuccess()) {
            return categoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }

    }

    /**
     * 根据商品分类ID，查询子节点，不递归
     *
     * @param session    session
     * @param categoryId categoryId
     * @return 平级子节点
     */
    @GetMapping("parallel_children_categories.do")
    public ServerResponse getParallelChildrenCategory(HttpSession session,
                                                      @RequestParam(value = "categoryId", defaultValue = "0") int categoryId) {

        // 校验用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }

        // 校验是否是管理员
        ServerResponse response = userService.checkUserRoleOfAdmin(userDO);

        if (response.isSuccess()) {
            // 查询子节点的category信息，并且不递归，保持平级
            return categoryService.listParallelChildrenCategories(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }

    }

    @GetMapping("deep_children_categoryIds.do")
    public ServerResponse getDeepChildrenCategoryIds(HttpSession session,
                                                     @RequestParam(value = "categoryId", defaultValue = "0") int categoryId) {

        // 校验用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }

        // 校验是否是管理员
        ServerResponse response = userService.checkUserRoleOfAdmin(userDO);

        if (response.isSuccess()) {
            // 查询当前节点的ID和递归子节点的ID
            return categoryService.listDeepChildrenCategoryIds(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }

    }

}
