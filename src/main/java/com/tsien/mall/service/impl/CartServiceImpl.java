package com.tsien.mall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tsien.mall.constant.CartStatusEnum;
import com.tsien.mall.constant.Const;
import com.tsien.mall.constant.ResponseCodeEnum;
import com.tsien.mall.dao.CartMapper;
import com.tsien.mall.dao.ProductMapper;
import com.tsien.mall.model.CartDO;
import com.tsien.mall.model.ProductDO;
import com.tsien.mall.service.CartService;
import com.tsien.mall.util.BigDecimalUtil;
import com.tsien.mall.util.PropertiesUtil;
import com.tsien.mall.util.ServerResponse;
import com.tsien.mall.vo.CartProductVO;
import com.tsien.mall.vo.CartVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/2 0002 16:16
 */

@Service
public class CartServiceImpl implements CartService {

    @Resource
    private CartMapper cartMapper;

    @Resource
    private ProductMapper productMapper;

    /**
     * 查询购物车
     *
     * @param userId userId
     * @return cartVO
     */
    @Override
    public ServerResponse<CartVO> list(Integer userId) {

        CartVO cartVO = this.getCartVoOfLimit(userId);
        return ServerResponse.createBySuccess(cartVO);

    }

    /**
     * 新增或者更新购物车
     *
     * @param userId    userId
     * @param productId productId
     * @param quantity  quantity
     * @return cartVO
     */
    @Override
    public ServerResponse<CartVO> insertOrUpdate(Integer userId, Integer productId, Integer quantity) {

        if (productId == null || quantity == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }

        CartDO cartDO = cartMapper.getCartByUserIdAndProductId(userId, productId);
        if (cartDO == null) {
            // 说明这个产品不在购物车里，需要新增这个产品
            CartDO cartItem = new CartDO();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setChecked(CartStatusEnum.CHECKED.getCode());
            cartMapper.insert(cartItem);
        } else {
            // 说明这个产品已经在库里了，数量相加
            quantity += cartDO.getQuantity();
            cartDO.setQuantity(quantity);
            cartMapper.updateByPrimaryKeySelective(cartDO);
        }

        CartVO cartVO = this.getCartVoOfLimit(userId);
        return ServerResponse.createBySuccess(cartVO);

    }

    /**
     * 更新购物车
     *
     * @param userId    userId
     * @param productId productId
     * @param quantity  quantity
     * @return CartVO
     */
    @Override
    public ServerResponse<CartVO> update(Integer userId, Integer productId, Integer quantity) {

        if (productId == null || quantity == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }

        CartDO cartDO = cartMapper.getCartByUserIdAndProductId(userId, productId);
        if (cartDO != null) {
            cartDO.setQuantity(quantity);
        }
        cartMapper.updateByPrimaryKeySelective(cartDO);

        CartVO cartVO = this.getCartVoOfLimit(userId);
        return ServerResponse.createBySuccess(cartVO);

    }

    /**
     * 删除购物车
     *
     * @param userId     userId
     * @param productIds productIds
     * @return 购物车
     */
    @Override
    public ServerResponse<CartVO> delete(Integer userId, String productIds) {

        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productIdList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }

        cartMapper.deleteByProductIds(userId, productIdList);

        CartVO cartVO = this.getCartVoOfLimit(userId);
        return ServerResponse.createBySuccess(cartVO);

    }

    /**
     * 设置全选或者全不选
     *
     * @param userId  userId
     * @param checked checked
     * @return CartVO
     */
    @Override
    public ServerResponse<CartVO> updateCartCheckedOrUnChecked(Integer userId, Integer productId, Integer checked) {

        cartMapper.updateCartCheckedOrUnChecked(userId, productId, checked);

        CartVO cartVO = this.getCartVoOfLimit(userId);
        return ServerResponse.createBySuccess(cartVO);

    }

    /**
     * 统计购物车商品数量
     *
     * @param userId userId
     * @return 商品数量
     */
    @Override
    public ServerResponse<Integer> countCartProducts(Integer userId) {

        if (userId == null) {
            return ServerResponse.createBySuccess(0);
        }

        return ServerResponse.createBySuccess(cartMapper.countCartProducts(userId));

    }


    /**
     * 组装cartVO
     *
     * @param userId userId
     * @return cartVO
     */
    private CartVO getCartVoOfLimit(Integer userId) {
        CartVO cartVO = new CartVO();
        List<CartDO> cartDoList = cartMapper.listCartsByUserId(userId);

        List<CartProductVO> cartProductVoList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if (CollectionUtils.isNotEmpty(cartDoList)) {

            // 将CartDO转换为CartProductVO
            for (CartDO cartDoItem : cartDoList) {
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(cartDoItem.getId());
                cartProductVO.setUserId(cartDoItem.getUserId());
                cartProductVO.setProductId(cartDoItem.getProductId());

                ProductDO productDO = productMapper.getByPrimaryKey(cartDoItem.getProductId());
                if (productDO != null) {
                    cartProductVO.setProductName(productDO.getName());
                    cartProductVO.setProductSubtitle(productDO.getSubtitle());
                    cartProductVO.setProductMainImage(productDO.getMainImage());
                    cartProductVO.setProductPrice(productDO.getPrice());
                    cartProductVO.setProductStatus(productDO.getStatus());
                    cartProductVO.setProductStock(productDO.getStock());
                    cartProductVO.setProductChecked(cartDoItem.getChecked());

                    // 判断库存
                    int buyLimitQuantity;
                    if (productDO.getStock() >= cartDoItem.getQuantity()) {
                        // 库存充足
                        buyLimitQuantity = cartDoItem.getQuantity();
                        cartProductVO.setLimitQuantity(Const.CartLimit.LIMIT_NUM_SUCCESS);
                    } else {
                        buyLimitQuantity = productDO.getStock();
                        cartProductVO.setLimitQuantity(Const.CartLimit.LIMIT_NUM_FAIL);
                        // 购物车中更新有效库存
                        CartDO cartForQuantity = new CartDO();
                        cartForQuantity.setId(cartDoItem.getId());
                        cartForQuantity.setQuantity(buyLimitQuantity);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVO.setQuantity(buyLimitQuantity);

                    // 计算商品总价
                    cartProductVO.setProductTotalPrice(BigDecimalUtil.multiply(productDO.getPrice().doubleValue(),
                            cartProductVO.getQuantity()));
                }

                if (cartDoItem.getChecked() == CartStatusEnum.CHECKED.getCode()) {
                    // 如果已勾选，将价格加入到购物车总价中,对cartProductVO.getProductTotalPrice()坐三元表达式是防止空指针异常.doubleValue()
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),
                            cartProductVO.getProductTotalPrice() == null ? 0.00 :
                                    cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVO);
            }
        }

        cartVO.setCartProductList(cartProductVoList);
        cartVO.setCartTotalPrice(cartTotalPrice);
        cartVO.setAllChecked(this.getCartOfChecked(userId));
        cartVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return cartVO;

    }

    /**
     * 查询购物车商品是不是全被勾选，true=全选，false=未被全选
     *
     * @param userId userId
     * @return 商品是否全被勾选
     */
    private boolean getCartOfChecked(Integer userId) {
        if (userId == null) {
            return false;
        }
        return cartMapper.countCartOfUnCheckedByUserId(userId) == 0;
    }


}
