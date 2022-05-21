package com.itheima.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.domain.Category;

public interface CategoryService {
    R save(Category category);
    R<Page> page(Integer page,Integer pageSize);
    R deleteById(Long id);
    R updateById(Category category);

    R getDishList(Integer type);

    Category selectById(Long categoryId);
}
