package com.tsien.mall.service;

import com.github.pagehelper.PageInfo;
import com.tsien.mall.model.ProductDO;
import com.tsien.mall.util.ServerResponse;
import com.tsien.mall.vo.ProductDetailVO;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/30 0030 12:09
 */

public interface ProductService {

    /**
     * 新增或者更新产品，根据前端传的productId是否为空判断
     *
     * @param productDO productDO
     * @return 新增或者更新的结果
     */
    ServerResponse saveOrUpdateProduct(ProductDO productDO);

    /**
     * 更新产品销售状态
     *
     * @param productId productId
     * @param status    status
     * @return 更新的结果
     */
    ServerResponse<String> updateProductSaleStatus(Integer productId, Integer status);

    /**
     * 获取商品详细信息
     *
     * @param productId productId
     * @return productDetailVO
     */
    ServerResponse<ProductDetailVO> getProductDetailOfManage(Integer productId);

    /**
     * 查询所有商品
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return pageInfo
     */
    ServerResponse<PageInfo> listProducts(int pageNum, int pageSize);

    /**
     * 搜索商品
     *
     * @param productName productName
     * @param productId   productId
     * @param pageNum     pageNum
     * @param pageSize    pageSize
     * @return pageInfo
     */
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    /**
     * 获取商品详情
     *
     * @param productId productId
     * @return 商品详情
     */
    ServerResponse<ProductDetailVO> getProductDetail(Integer productId);

    /**
     * 根据关键字搜索商品
     *
     * @param keyword    keyword
     * @param categoryId categoryId
     * @param pageNum    pageNum
     * @param pageSize   pageSize
     * @param orderBy    orderBy
     * @return pageInfo
     */
    ServerResponse<PageInfo> listProductsByKeywordAndCategoryId(String keyword, Integer categoryId,
                                                                int pageNum, int pageSize, String orderBy);

}
