package com.tsien.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/30 0030 15:18
 */

@Data
public class ProductListVO {

    /**
     * 商品ID
     */
    private Integer id;

    /**
     * 分类ID,对应mall_category表的主键
     */
    private Integer categoryId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品副标题
     */
    private String subtitle;

    /**
     * 图片服务器Url前缀
     */
    private String imageHost;

    /**
     * 产品主图,url相对地址
     */
    private String mainImage;

    /**
     * 价格,单位-元保留两位小数
     */
    private BigDecimal price;

    /**
     * 商品状态.1-在售 2-下架 3-删除
     */
    private Integer status;
}
