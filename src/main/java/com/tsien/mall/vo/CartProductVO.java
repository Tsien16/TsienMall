package com.tsien.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/2 0002 16:50
 */

@Data
public class CartProductVO {

    /**
     * 购物车ID
     */
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 商品ID
     */
    private Integer productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品标题
     */
    private String productSubtitle;

    /**
     * 商品主图
     */
    private String productMainImage;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 商品单价
     */
    private BigDecimal productPrice;

    /**
     * 商品状态
     */
    private Integer productStatus;

    /**
     * 商品总价
     */
    private BigDecimal productTotalPrice;

    /**
     * 商品库存
     */
    private Integer productStock;

    /**
     * 商品是否已勾选
     */
    private Integer productChecked;

    /**
     * 限制的数量
     */
    private String limitQuantity;
}
