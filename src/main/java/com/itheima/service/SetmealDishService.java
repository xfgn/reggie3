package com.itheima.service;

import com.itheima.domain.SetmealDish;

import java.util.List;

public interface SetmealDishService {

    void save(SetmealDish setmealDish);

    List<SetmealDish> selectBySetmealId(Long setmealId);

    void deleteBatchBysetmealIds(List<Long> ids);
}
