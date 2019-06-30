package com.tsien.mall.controller.protal;

import com.tsien.mall.service.ProductService;
import com.tsien.mall.util.ServerResponse;
import com.tsien.mall.vo.ProductDetailVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/1 0001 1:19
 */

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    /**
     * 获取商品详情
     *
     * @param productId productId
     * @return 商品详情
     */
    @GetMapping("detail.do")
    public ServerResponse<ProductDetailVO> detail(Integer productId) {
        return productService.getProductDetail(productId);
    }

    /**
     * 查询商品
     *
     * @param keyword    keyword
     * @param categoryId categoryId
     * @param pageNum    pageNum
     * @param pageSize   pageSize
     * @param orderBy    orderBy
     * @return 查询结果
     */
    @GetMapping("list.do")
    public ServerResponse list(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "categoryId", required = false) Integer categoryId,
                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                               @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {

        return productService.listProductsByKeywordAndCategoryId(keyword, categoryId, pageNum, pageSize, orderBy);
    }
}
