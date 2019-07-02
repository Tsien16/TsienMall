package com.tsien.mall.controller.backend;

import com.google.common.collect.Maps;
import com.tsien.mall.constant.Const;
import com.tsien.mall.constant.ResponseCodeEnum;
import com.tsien.mall.model.ProductDO;
import com.tsien.mall.model.UserDO;
import com.tsien.mall.service.FileService;
import com.tsien.mall.service.ProductService;
import com.tsien.mall.service.UserService;
import com.tsien.mall.util.PropertiesUtil;
import com.tsien.mall.util.ServerResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

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

    @Resource
    private FileService fileService;

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

    /**
     * 搜索商品
     *
     * @param session     session
     * @param productName productName
     * @param productId   productId
     * @param pageNum     pageNum
     * @param pageSize    pageSize
     * @return 搜索结果
     */
    @GetMapping("search.do")
    public ServerResponse searchProduct(HttpSession session,
                                        @RequestParam(value = "productName", required = false) String productName,
                                        @RequestParam(value = "productId", required = false) Integer productId,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        // 检查用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请使用管理员账号登陆");
        }

        // 检查用户是否是管理员,是管理员就更新
        if (userService.checkUserRoleOfAdmin(userDO).isSuccess()) {
            return productService.searchProduct(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，无权限操作");
        }
    }

    /**
     * 文件上传
     *
     * @param file    file
     * @param request request
     * @return 上传的地址
     */
    @PostMapping("upload.do")
    public ServerResponse upload(HttpSession session,
                                 @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                 HttpServletRequest request) {

        // 检查用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(), "用户未登录，请使用管理员账号登陆");
        }

        // 检查用户是否是管理员,是管理员就更新
        if (userService.checkUserRoleOfAdmin(userDO).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);

            return ServerResponse.createBySuccess(fileMap);

        } else {
            return ServerResponse.createByErrorMessage("不是管理员用户，无权限操作");
        }
    }

    @PostMapping("richText_image_upload.do")
    public Map richTextImageUpload(HttpSession session,
                                   @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        Map resultMap = Maps.newHashMap();
        // 检查用户是否登陆
        UserDO userDO = (UserDO) session.getAttribute(Const.CURRENT_USER);
        if (userDO == null) {
            resultMap.put("success", false);
            resultMap.put("msg", "不是管理员用户,请登陆");
            return resultMap;
        }

        // 检查用户是否是管理员,是管理员就更新
        if (userService.checkUserRoleOfAdmin(userDO).isSuccess()) {

            // 富文本对返回值有自己的要求
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }

            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;

        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "不是管理员用户，无权限操作");
            return resultMap;
        }
    }


}
