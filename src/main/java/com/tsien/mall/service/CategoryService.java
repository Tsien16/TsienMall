package com.tsien.mall.service;

import com.tsien.mall.model.CategoryDO;
import com.tsien.mall.util.ServerResponse;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/29 0029 12:24
 */

public interface CategoryService {

    /**
     * 添加商品分类
     *
     * @param categoryName categoryName
     * @param parentId     parentId
     * @return 添加的结果
     */
    ServerResponse addCategory(String categoryName, Integer parentId);

    /**
     * 更新商品分类名称
     *
     * @param categoryId   categoryId
     * @param categoryName categoryName
     * @return 更新的结果
     */
    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    /**
     * 根据分类ID查询下级子分类，不递归
     *
     * @param categoryId categoryId
     * @return categoryList
     */
    ServerResponse<List<CategoryDO>> listParallelChildrenCategories(Integer categoryId);

    /**
     * 递归查询所有的子节点
     *
     * @param categoryId categoryId
     * @return 所有的子节点
     */
    ServerResponse<List<Integer>> listDeepChildrenCategoryIds(Integer categoryId);

}
