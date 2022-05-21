package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.CustomException;
import com.itheima.common.R;
import com.itheima.dao.CategoryDao;
import com.itheima.dao.DishDao;
import com.itheima.dao.SetmealDao;
import com.itheima.domain.Category;
import com.itheima.domain.Dish;
import com.itheima.domain.Setmeal;
import com.itheima.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private DishDao dishDao;
    @Autowired
    private SetmealDao setmealDao;

    @Override
    public R save(Category category) {
        int count=categoryDao.insert(category);
        if(count!=1){
            return R.error("添加失败！");
        }
        return R.success(null);
    }

    @Override
    public R<Page> page(Integer page, Integer pageSize) {
        Page<Category> ipage=new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        queryWrapper.eq(Category::getIsDeleted,1);
        categoryDao.selectPage(ipage,queryWrapper);
        return R.success(ipage);
    }

    @Override
    public R deleteById(Long id) {
        /*若当前分类含有菜品或套餐则不能删除*/
        //检查当前分类是否含有菜品
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,id);
        int count1=dishDao.selectCount(queryWrapper);
        if(count1>0){
            //抛异常
            throw new CustomException("删除失败，当前分类下含有其它菜品!");
        }

        //检查当前分类是否含有套餐
        LambdaQueryWrapper<Setmeal> queryWrapper2=new LambdaQueryWrapper<>();
        queryWrapper2.eq(Setmeal::getCategoryId,id);
        int count2=setmealDao.selectCount(queryWrapper2);
        if(count2>0){
            //抛异常
            throw new CustomException("删除失败，当前分类下含有套餐!");
        }
        //未抛出异常，则删除
        Category category=new Category();
        category.setId(id);
        category.setIsDeleted(0);
        int count=categoryDao.updateById(category);
        if(count==1){
            return R.success(null);
        }
        return R.error("删除失败!");
    }

    @Override
    public R updateById(Category category) {
        int count=categoryDao.updateById(category);
        if(count==1){
            return R.success(null);
        }
        return R.error("修改失败！");
    }

    @Override
    /*查找所有的菜品分类或套餐分类*/
    public R getDishList(Integer type) {
        /*type=1 菜品分类，type=2 套餐分类。差找所有的type对应类型种类
        * 若type为空，则不分类型，菜品分类和套餐分类全部查出来*/
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(type!=null,Category::getType,type);
        queryWrapper.eq(Category::getIsDeleted,1);
        queryWrapper.orderByDesc(Category::getType);//排序
        queryWrapper.orderByAsc(Category::getSort);
        queryWrapper.orderByDesc(Category::getUpdateTime);
        List<Category> categoryList=categoryDao.selectList(queryWrapper);
        return R.success(categoryList);
    }

    @Override
    public Category selectById(Long categoryId) {
        return categoryDao.selectById(categoryId);
    }
}
