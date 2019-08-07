package com.tsien.mall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tsien.mall.dao.CategoryMapper;
import com.tsien.mall.model.CategoryDO;
import com.tsien.mall.service.CategoryService;
import com.tsien.mall.util.ServerResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/29 0029 12:25
 */

@Service
public class CategoryServiceImpl implements CategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 添加商品分类
     *
     * @param categoryName categoryName
     * @param parentId     parentId
     * @return 添加的结果
     */
    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        CategoryDO categoryDO = new CategoryDO();
        categoryDO.setParentId(parentId);
        categoryDO.setName(categoryName);
        categoryDO.setStatus(true);

        int rowCount = categoryMapper.insert(categoryDO);

        if (rowCount > 0) {
            return ServerResponse.createBySuccess("添加品类成功");
        }

        return ServerResponse.createByErrorMessage("添加品类失败");

    }

    /**
     * 更新商品分类名称
     *
     * @param categoryId   categoryId
     * @param categoryName categoryName
     * @return 更新的结果
     */
    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {

        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类名称参数错误");
        }

        CategoryDO categoryDO = new CategoryDO();
        categoryDO.setId(categoryId);
        categoryDO.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(categoryDO);

        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新品类名称成功");
        }

        return ServerResponse.createByErrorMessage("更新品类名称失败");
    }

    /**
     * 根据分类ID查询下级子分类，不递归
     *
     * @param categoryId categoryId
     * @return categoryList
     */
    @Override
    public ServerResponse<List<CategoryDO>> listParallelChildrenCategories(Integer categoryId) {

        List<CategoryDO> categoryList = categoryMapper.listChildrenCategoriesByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到当前分类的子分类");
        }

        return ServerResponse.createBySuccess(categoryList);

    }

    /**
     * 查询子节点
     *
     * @param categoryId categoryId
     * @return 子节点
     */
    @Override
    public ServerResponse<List<Integer>> listDeepChildrenCategoryIds(Integer categoryId) {

        Set<CategoryDO> categorySet = Sets.newHashSet();
        this.listChildrenCategories(categorySet, categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();

        if (categoryId != null) {
            for (CategoryDO categoryItem : categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    /**
     * 递归查询商品分类
     *
     * @param categorySet categorySet
     * @param categoryId  categoryId
     */
    private void listChildrenCategories(Set<CategoryDO> categorySet, Integer categoryId) {

        // 根据categoryId查找当前商品分类，并且放进Set集合里：categorySet
        CategoryDO categoryDO = categoryMapper.getByPrimaryKey(categoryId);
        if (categoryDO != null) {
            categorySet.add(categoryDO);
        }

        //查找子节点，递归算法一定要有一个退出条件,退出条件是：子节点是否为空
        List<CategoryDO> categoryList = categoryMapper.listChildrenCategoriesByParentId(categoryId);
        for (CategoryDO categoryItem : categoryList) {
            listChildrenCategories(categorySet, categoryItem.getId());
        }

    }

}
