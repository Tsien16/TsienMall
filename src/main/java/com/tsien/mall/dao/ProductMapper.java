package com.tsien.mall.dao;

import com.tsien.mall.model.ProductDO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/22 0022 15:31
 */

public interface ProductMapper {

    /**
     * 通过主键删除商品
     *
     * @param id 商品ID
     * @return 删除的数量
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入商品数据
     *
     * @param record 商品实体
     * @return 插入的数量
     */
    int insert(ProductDO record);

    /**
     * 选择性的插入商品数据
     *
     * @param record 商品实体
     * @return 插入的数量
     */
    int insertSelective(ProductDO record);

    /**
     * 通过主键查询商品
     *
     * @param id 商品ID
     * @return 商品实体
     */
    ProductDO getByPrimaryKey(Integer id);

    /**
     * 通过主键选择性更新商品
     *
     * @param record 商品实体
     * @return 更新的数量
     */
    int updateByPrimaryKeySelective(ProductDO record);

    /**
     * 通过主键更新商品
     *
     * @param record 商品实体
     * @return 更新的数量
     */
    int updateByPrimaryKey(ProductDO record);

    /**
     * 查询所有的商品
     *
     * @return productList
     */
    List<ProductDO> listProducts();
}