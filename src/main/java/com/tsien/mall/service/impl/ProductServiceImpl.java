package com.tsien.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tsien.mall.constant.Const;
import com.tsien.mall.constant.ProductStatusEnum;
import com.tsien.mall.constant.ResponseCodeEnum;
import com.tsien.mall.dao.CategoryMapper;
import com.tsien.mall.dao.ProductMapper;
import com.tsien.mall.model.CategoryDO;
import com.tsien.mall.model.ProductDO;
import com.tsien.mall.service.CategoryService;
import com.tsien.mall.service.ProductService;
import com.tsien.mall.util.DateTimeUtil;
import com.tsien.mall.util.PropertiesUtil;
import com.tsien.mall.util.ServerResponse;
import com.tsien.mall.vo.ProductDetailVO;
import com.tsien.mall.vo.ProductListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/30 0030 12:10
 */

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private CategoryService categoryService;

    /**
     * 新增或者更新产品，根据前端传的productId是否为空判断
     *
     * @param productDO productDO
     * @return 新增或者更新的结果
     */
    @Override
    public ServerResponse saveOrUpdateProduct(ProductDO productDO) {
        if (productDO != null) {

            if (StringUtils.isNotBlank(productDO.getSubImages())) {
                String[] subImageArray = productDO.getMainImage().split(",");
                if (subImageArray.length > 0) {
                    productDO.setMainImage(subImageArray[0]);
                }
            }

            // 更新或者新增产品
            if (productDO.getId() != null) {
                int rowCount = productMapper.updateByPrimaryKey(productDO);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("更新产品成功");
                }
                return ServerResponse.createByErrorMessage("更新产品失败");

            } else {
                int rowCount = productMapper.insert(productDO);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccess("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("新增产品失败");
            }
        }

        return ServerResponse.createByErrorMessage("新增或者更新产品参数不正确");

    }

    /**
     * 更新产品销售状态
     *
     * @param productId productId
     * @param status    status
     * @return 更新的结果
     */
    @Override
    public ServerResponse<String> updateProductSaleStatus(Integer productId, Integer status) {

        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }


        ProductDO productDO = new ProductDO();
        productDO.setId(productId);
        productDO.setStatus(status);

        int rowCount = productMapper.updateByPrimaryKeySelective(productDO);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("修改产品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("修改产品销售状态失败");

    }

    /**
     * 获取商品详细信息
     *
     * @param productId productId
     * @return productDetailVO
     */
    @Override
    public ServerResponse<ProductDetailVO> getProductDetailOfManage(Integer productId) {

        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }

        ProductDO productDO = productMapper.getByPrimaryKey(productId);
        if (productDO == null) {
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }

        ProductDetailVO productDetailVO = this.assembleProductDetailVO(productDO);

        return ServerResponse.createBySuccess(productDetailVO);

    }

    /**
     * 查询所有商品
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return pageInfo
     */
    @Override
    public ServerResponse<PageInfo> listProducts(int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        List<ProductDO> productList = productMapper.listProducts();

        List<ProductListVO> productListVos = Lists.newArrayList();

        // 将Product 转换为 ProductListVO
        for (ProductDO productItem : productList) {
            ProductListVO productListVoItem = this.assembleProductListVO(productItem);
            productListVos.add(productListVoItem);
        }

        PageInfo pageInfo = new PageInfo<>(productListVos);

        return ServerResponse.createBySuccess(pageInfo);

    }

    /**
     * 搜索商品
     *
     * @param productName productName
     * @param productId   productId
     * @param pageNum     pageNum
     * @param pageSize    pageSize
     * @return pageInfo
     */
    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        if (StringUtils.isNotBlank(productName)) {
            productName = "%" + productName + "%";
        }

        List<ProductDO> productList = productMapper.listProductsByProductNameAndProductId(productName, productId);

        // 将Product 转换为 ProductListVO java8函数式写法
        List<ProductListVO> productListVos = productList.stream()
                .map(this::assembleProductListVO)
                .collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo<>(productListVos);

        return ServerResponse.createBySuccess(pageInfo);

    }

    /**
     * 获取商品详情
     *
     * @param productId productId
     * @return 商品详情
     */
    @Override
    public ServerResponse<ProductDetailVO> getProductDetail(Integer productId) {

        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }

        ProductDO productDO = productMapper.getByPrimaryKey(productId);
        if (productDO == null) {
            return ServerResponse.createByErrorMessage("商品已下架或者删除");
        }

        if (productDO.getStatus() != ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("商品已下架或者删除");
        }

        ProductDetailVO productDetailVO = this.assembleProductDetailVO(productDO);

        return ServerResponse.createBySuccess(productDetailVO);

    }

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
    @Override
    public ServerResponse<PageInfo> listProductsByKeywordAndCategoryId(String keyword, Integer categoryId,
                                                                       int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }

        List<Integer> categoryIdList = new ArrayList<>();
        if (categoryId != null) {
            CategoryDO categoryDO = categoryMapper.getByPrimaryKey(categoryId);
            if (categoryDO == null && StringUtils.isBlank(keyword)) {
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVO> productListVos = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo<>(productListVos);

                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = categoryService.listDeepChildrenCategoryIds(categoryDO.getId()).getData();
        }

        if (StringUtils.isNotBlank(keyword)) {
            keyword = "%" + keyword + "%";
        }

        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        List<ProductDO> productList =
                productMapper.listProductsByProductNameAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword,
                        categoryIdList.size() == 0 ? null : categoryIdList);

        List<ProductListVO> productListVoList = Lists.newArrayList();
        for (ProductDO productItem : productList) {
            ProductListVO productListVO = this.assembleProductListVO(productItem);
            productListVoList.add(productListVO);
        }

        PageInfo pageInfo = new PageInfo<>(productListVoList);

        return ServerResponse.createBySuccess(pageInfo);


    }


    /**
     * 组装productDetailVO方法
     *
     * @param productDO productDO
     * @return productDetailVO
     */
    private ProductDetailVO assembleProductDetailVO(ProductDO productDO) {

        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setId(productDO.getId());
        productDetailVO.setCategoryId(productDO.getCategoryId());
        productDetailVO.setName(productDO.getName());
        productDetailVO.setSubtitle(productDO.getSubtitle());
        productDetailVO.setMainImage(productDO.getMainImage());
        productDetailVO.setSubImages(productDO.getSubImages());
        productDetailVO.setDetail(productDO.getDetail());
        productDetailVO.setPrice(productDO.getPrice());
        productDetailVO.setStock(productDO.getStock());
        productDetailVO.setStatus(productDO.getStatus());

        CategoryDO categoryDO = categoryMapper.getByPrimaryKey(productDO.getCategoryId());
        if (categoryDO == null) {
            productDetailVO.setParentCategoryId(0);
        } else {
            productDetailVO.setParentCategoryId(categoryDO.getParentId());
        }

        productDetailVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        productDetailVO.setCreateTime(DateTimeUtil.dateTimeToString(productDO.getCreateTime()));
        productDetailVO.setUpdateTime(DateTimeUtil.dateTimeToString(productDO.getUpdateTime()));

        return productDetailVO;

    }

    /**
     * 组装productListVO方法
     *
     * @param productDO productDO
     * @return productListVO
     */
    private ProductListVO assembleProductListVO(ProductDO productDO) {

        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(productDO.getId());
        productListVO.setCategoryId(productDO.getCategoryId());
        productListVO.setName(productDO.getName());
        productListVO.setSubtitle(productDO.getSubtitle());
        productListVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        productListVO.setMainImage(productDO.getMainImage());
        productListVO.setPrice(productDO.getPrice());
        productListVO.setStatus(productDO.getStatus());

        return productListVO;

    }


}
