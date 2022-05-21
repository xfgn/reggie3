package com.itheima.service;

import com.itheima.common.R;
import com.itheima.domain.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    List<ShoppingCart> getCartListByUserId(Long userId);

    R<ShoppingCart> insert(ShoppingCart shoppingCart);

    void cleanShoppingCart(Long userId);

    R<ShoppingCart> updateNumber(ShoppingCart shoppingCart);
}
