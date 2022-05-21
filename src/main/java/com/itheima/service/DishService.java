package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.PageR;
import com.itheima.common.R;
import com.itheima.domain.Dish;
import com.itheima.dto.DishDto;

import java.util.List;

public interface DishService {

    R<PageR> page(Integer page, Integer pageSize, String name);

    void saveWithFlavor(DishDto dishDto);

    R<DishDto> getDishWithFlavorById(Long id);

    void updateWithFlavor(DishDto dishDto);

    void updateStatusById(Integer status, String[] str);

    R deleteBatch(String[] str);

    R<List<DishDto>>selectByCategoryId(Dish dish);
}
