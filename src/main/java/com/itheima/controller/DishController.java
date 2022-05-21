package com.itheima.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.PageR;
import com.itheima.common.R;
import com.itheima.dao.DishDao;
import com.itheima.domain.Dish;
import com.itheima.dto.DishDto;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    /*分页查询*/
    @GetMapping("/page")
    public R<PageR> page(Integer page, Integer pageSize, String name){
        return dishService.page(page,pageSize,name);
    }

    /*根据id查Dish---回显菜品信息*/
    @GetMapping("/{id}")
    public R<DishDto> getDishById(@PathVariable Long id){
        return dishService.getDishWithFlavorById(id);
    }

    //新增菜品
    @PostMapping
    public R save(@RequestBody DishDto dishDto){
        /*新增菜品时，若有口味信息，还应将口味信息插入dish_flavor表中*/
        dishService.saveWithFlavor(dishDto);
        return R.success(null);
    }

    //修改菜品
    @PutMapping
    public R update(@RequestBody DishDto dishDto){
        /*修改菜品时，若有口味信息，还应修改口味信息在dish_flavor表中*/
        dishService.updateWithFlavor(dishDto);
        return R.success(null);
    }

    //更改菜品状态--停售or起售/批量停售起售
    @PostMapping("/status/{status}")
    public R updateStausById(@PathVariable Integer status, String ids){
        String[] str=ids.split(",");
        dishService.updateStatusById(status,str);
        return R.success(null);
    }
    //删除或批量删除
    @DeleteMapping
    public R delete(String ids){
        String[] str=ids.split(",");
        return dishService.deleteBatch(str);
    }

    //通过categoryId获取该分类下的dish,口味也要一同查出来
    @GetMapping("/list")
    public R<List<DishDto>> getDishList(Dish dish){
        return dishService.selectByCategoryId(dish);
    }
}
