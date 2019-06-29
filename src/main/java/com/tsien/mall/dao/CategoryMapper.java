package com.tsien.mall.dao;

import com.tsien.mall.model.CategoryDO;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/22 0022 15:31
 */

public interface CategoryMapper {

    /**
     * 通过主键删除类别
     *
     * @param id 类别ID
     * @return 删除的数量
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 插入类别数据
     *
     * @param record 类别实体
     * @return 插入的数量
     */
    int insert(CategoryDO record);

    /**
     * 选择性的插入类别数据
     *
     * @param record 类别实体
     * @return 插入的数量
     */
    int insertSelective(CategoryDO record);

    /**
     * 通过主键查询类别
     *
     * @param id 类别ID
     * @return 类别实体
     */
    CategoryDO getByPrimaryKey(Integer id);

    /**
     * 通过主键选择性更新类别
     *
     * @param record 类别实体
     * @return 更新的数量
     */
    int updateByPrimaryKeySelective(CategoryDO record);

    /**
     * 通过主键更新类别
     *
     * @param record 类别实体
     * @return 更新的数量
     */
    int updateByPrimaryKey(CategoryDO record);

    /**
     * 根据parentId查询商品分类
     *
     * @param parentId parentId
     * @return 子分类
     */
    List<CategoryDO> listChildrenCategoriesByParentId(Integer parentId);
}