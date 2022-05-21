package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.common.R;
import com.itheima.dao.ShoppingCartDao;
import com.itheima.domain.ShoppingCart;
import com.itheima.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartDao shoppingCartDao;

    //查询购物车内全部商品
    @Override
    public List<ShoppingCart> getCartListByUserId(Long userId) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        return shoppingCartDao.selectList(queryWrapper);
    }

    //往购物车里添加商品
    @Override
    public R<ShoppingCart> insert(ShoppingCart shoppingCart) {
        //每次添加，数量默认为1，若下次添加的为同一菜品，直接改number即可
        //先查询当前菜品是否存在，不存在，直接插入;存在，数量加1
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        queryWrapper.eq(shoppingCart.getUserId()!=null,ShoppingCart::getUserId,shoppingCart.getUserId());
        int count=shoppingCartDao.selectCount(queryWrapper);
        if (count > 0) {
            shoppingCart.setNumber(count+1);
            shoppingCartDao.update(shoppingCart,queryWrapper);
        }else{
            shoppingCartDao.insert(shoppingCart);
            shoppingCart.setNumber(1);
        }
        return R.success(shoppingCart);
    }

    //清空购物车
    @Override
    public void cleanShoppingCart(Long userId) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartDao.delete(queryWrapper);
    }

    //减少购物车商品数量，-1
    @Override
    public R<ShoppingCart> updateNumber(ShoppingCart shoppingCart) {
        //先查一下该商品加购的数量
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(shoppingCart.getDishId()!=null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        queryWrapper.eq(shoppingCart.getUserId()!=null,ShoppingCart::getUserId,shoppingCart.getUserId());
        ShoppingCart cart = shoppingCartDao.selectOne(queryWrapper);
        //若数量减1后为0则删除
        int count=cart.getNumber()-1;
        shoppingCart.setNumber(count);
        if(count==0){//删除
            shoppingCartDao.deleteById(cart.getId());
        }else{//更新
            shoppingCartDao.update(shoppingCart,queryWrapper);
        }
        return R.success(shoppingCart);
    }

}
