package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.dao.DishFlavorDao;
import com.itheima.domain.DishFlavor;
import com.itheima.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishFlavorServiceImpl implements DishFlavorService {
    @Autowired
    private DishFlavorDao dishFlavorDao;

    @Override
    public void insertBatch(List<DishFlavor> list) {
        for(DishFlavor dishFlavor:list){
            dishFlavorDao.insert(dishFlavor);
        }
    }

    @Override
    public List<DishFlavor> selectByDishId(Long id) {
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        return dishFlavorDao.selectList(queryWrapper);
    }

    @Override
    public void deleteByDishId(Long id) {
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        dishFlavorDao.delete(queryWrapper);
    }
}
