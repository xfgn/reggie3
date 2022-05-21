package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.common.PageR;
import com.itheima.dao.DishDao;
import com.itheima.domain.Dish;
import com.itheima.domain.DishFlavor;
import com.itheima.dto.DishDto;
import com.itheima.service.CategoryService;
import com.itheima.service.DishFlavorService;
import com.itheima.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DishSeviceImpl implements DishService {
    @Autowired
    private DishDao dishDao;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public R<PageR> page(Integer page, Integer pageSize, String name) {
        /*法一：*/
       /*Page<Dish> ipage=new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name),Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishDao.selectPage(ipage,queryWrapper);
        与前端页面匹配
        Page<DishDto> pageInfo=new Page<>(page,pageSize);
        BeanUtils.copyProperties(ipage,pageInfo,"records");复制属性,但records属性不复制，因为泛型不同，无法转换
        List<Dish> dishList=ipage.getRecords();
        List<DishDto> dishDtos=new ArrayList<>();
        for(Dish dish:dishList){
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(dish,dishDto);
            Long categoryId=dish.getCategoryId();
            Category category=categoryService.selectById(categoryId);
            dishDto.setCategoryName(category.getName());
            dishDtos.add(dishDto);
        }
        pageInfo.setRecords(dishDtos);
        return R.success(pageInfo);*/

        /*法二：*/
        List<DishDto> dishDtoList=dishDao.selectDishDto((page-1)*pageSize,pageSize);
        long total=dishDao.selectCount(null);
        long pages=total/pageSize==0?total/pageSize:(total/pageSize)+1;
        return R.success(new PageR(dishDtoList,total,pages,pageSize,page));
    }


    @Override
    //事务,插入Dish以及其关联DishFlavor
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        dishDao.insert(dishDto);
        Long dishId=dishDto.getId();
        List<DishFlavor> list=dishDto.getFlavors();
        for(DishFlavor dishFlavor:list){
            dishFlavor.setDishId(dishId);
        }
        dishFlavorService.insertBatch(list);//批量插入
    }

    //回显菜品信息
    @Override
    public R<DishDto> getDishWithFlavorById(Long id) {
        Dish dish=dishDao.selectById(id);
        DishDto dishDto=new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        List<DishFlavor> list=dishFlavorService.selectByDishId(dish.getId());
        dishDto.setFlavors(list);
        return R.success(dishDto);
    }

    //更新菜品信息，Dish和其关联DishFlavor
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        dishDao.updateById(dishDto);
        //先删除dishFlavor中数据，再重新批量添加
        dishFlavorService.deleteByDishId(dishDto.getId());
        List<DishFlavor> list=dishDto.getFlavors();
        for(DishFlavor dishFlavor:list){
            dishFlavor.setDishId(dishDto.getId());
        }
        dishFlavorService.insertBatch(list);//批量插入dishFlavor
    }

    /*修改菜品起售停售状态*/
    @Override
    public void updateStatusById(Integer status, String[] str) {
        for(String id:str){
            Dish dish=new Dish();
            dish.setId(Long.valueOf(id));
            dish.setStatus(status);
            dishDao.updateById(dish);
        }
    }

    @Override
    @Transactional
    public R deleteBatch(String[] str) {
        List<Dish> dishList = dishDao.selectBatchIds(Arrays.asList(str));
        dishDao.deleteBatchIds(Arrays.asList(str));
        //菜品被删除那么DishFlavor也应该被删掉
        for(Dish dish:dishList){
            dishFlavorService.deleteByDishId(dish.getId());
        }
        return R.success(null);
    }

    //根据categoryID查询该分类的所有菜品dish,包括菜品口味
    @Override
    public R<List<DishDto>> selectByCategoryId(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,dish.getStatus());
        //select * from dish where categoryId=? and status=1;
        List<Dish> dishList=dishDao.selectList(queryWrapper);
        List<DishDto> dishDtos=new ArrayList<>();
        for(Dish dish1:dishList){
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(dish1,dishDto);
            dishDto.setFlavors(dishFlavorService.selectByDishId(dish1.getId()));
            dishDtos.add(dishDto);
        }
        return R.success(dishDtos);
    }


}
