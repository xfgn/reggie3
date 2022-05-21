package com.itheima.service;

import com.itheima.common.R;
import com.itheima.domain.Dish;
import com.itheima.domain.DishFlavor;

import java.util.List;

public interface DishFlavorService {

    void insertBatch(List<DishFlavor> list);

    List<DishFlavor> selectByDishId(Long id);

    void deleteByDishId(Long id);

}
