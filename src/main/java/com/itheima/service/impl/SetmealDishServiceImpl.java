package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itheima.dao.SetmealDishDao;
import com.itheima.domain.SetmealDish;
import com.itheima.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealDishServiceImpl implements SetmealDishService {
   @Autowired
   private SetmealDishDao setmealDishDao;

    //插入
    @Override
    public void save(SetmealDish setmealDish) {
        setmealDishDao.insert(setmealDish);
    }

    @Override
    public List<SetmealDish> selectBySetmealId(Long setmealId) {
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealId);
        return setmealDishDao.selectList(queryWrapper);
    }

    @Override
    public void deleteBatchBysetmealIds(List<Long> ids) {
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishDao.delete(queryWrapper);
    }



}
