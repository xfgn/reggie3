package com.itheima.service;

import com.itheima.common.PageR;
import com.itheima.common.R;
import com.itheima.domain.Setmeal;
import com.itheima.dto.SetmealDto;

import java.util.List;

public interface SetmealService {

    R<PageR> page(Integer page, Integer pageSize, String name);

    void save(SetmealDto setmealDto);

    R<SetmealDto> getSetmealWithSetmealDishById(Long setmealId);

    void deleteByIds(List<Long> ids);

    List<SetmealDto> getListByCategotyId(Setmeal setmeal);
}
