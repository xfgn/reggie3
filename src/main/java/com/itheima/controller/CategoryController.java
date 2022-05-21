package com.itheima.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.domain.Category;
import com.itheima.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R addCategory(@RequestBody Category category){
        return categoryService.save(category);
    }

    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize){
        return categoryService.page(page,pageSize);
    }

    @DeleteMapping
    public R deleteById(Long ids){
        return categoryService.deleteById(ids);
    }

    @PutMapping
    public R updateById(@RequestBody Category category){
        return categoryService.updateById(category);
    }

    /*获取所有的菜品分类或套餐分类名称*/
    @GetMapping("/list")
    public R getDishList(Integer type){
        return categoryService.getDishList(type);
    }
}
