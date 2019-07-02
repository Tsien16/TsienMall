package com.tsien.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/2 0002 17:07
 */

@Data
public class CartVO {

    /**
     * 购物车商品列别
     */
    private List<CartProductVO> cartProductList;

    /**
     * 购物车总价
     */
    private BigDecimal cartTotalPrice;

    /**
     * 购物车是否全部勾选
     */
    private Boolean allChecked;

    /**
     * 购物车主图
     */
    private String imageHost;

}
