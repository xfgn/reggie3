package com.itheima.controller;

import com.itheima.common.R;
import com.itheima.domain.ShoppingCart;
import com.itheima.service.ShoppingCartService;
import com.itheima.utils.BaseContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    //查询购物车内全部商品
    @GetMapping("/list")
    public R<List<ShoppingCart>> getCartList(){
        return R.success(shoppingCartService.getCartListByUserId(BaseContextUtil.get()));
    }

    //往购物车里添加商品（+）
    @PostMapping("/add")
    public R<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCart.setUserId(BaseContextUtil.get());
       return shoppingCartService.insert(shoppingCart);
    }

    //减少购物车中商品，点击（-）
    @PostMapping("/sub")
    public R<ShoppingCart> updateCart(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContextUtil.get());
        //更新是指，数量-1
        return shoppingCartService.updateNumber(shoppingCart);
    }


    //清空购物车
    @DeleteMapping("/clean")
    public R cleanShoppingCart(){
        //根据登录账号清空购物车信息
        shoppingCartService.cleanShoppingCart(BaseContextUtil.get());
        return R.success(null);
    }
}
