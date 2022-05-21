package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itheima.common.CustomException;
import com.itheima.common.PageR;
import com.itheima.common.R;
import com.itheima.dao.SetmealDao;
import com.itheima.domain.Category;
import com.itheima.domain.Setmeal;
import com.itheima.domain.SetmealDish;
import com.itheima.dto.DishDto;
import com.itheima.dto.SetmealDto;
import com.itheima.service.CategoryService;
import com.itheima.service.SetmealDishService;
import com.itheima.service.SetmealService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;

    //分页查询
    @Override
    public R<PageR> page(Integer page, Integer pageSize, String name) {
        //联表查询，mybatisplus实现不了，因此自己写分页，用limit,IPage和queryWrapper均无法使用!
        List<Setmeal> setmealList=null;
        if(Strings.isEmpty(name)){
            //name为空，查全部
            setmealList=setmealDao.selectPageAll((page-1)*pageSize,pageSize);
        }else{
            //根据name模糊查询
            setmealList=setmealDao.selectPageByName((page-1)*pageSize,pageSize,name);
        }
        //数据总量
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.like(Strings.isEmpty(name),"name",name);
        Integer integer = setmealDao.selectCount(queryWrapper);
        long total=Long.valueOf(integer);
        //最大页码数
        long pages=(total%pageSize)==0?total/pageSize:(total/pageSize)+1;
        List<DishDto> dishDtos=new ArrayList<>();
        //查套餐分类名称categoryName
        for(Setmeal setmeal:setmealList){
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(setmeal,dishDto);
            Category category = categoryService.selectById(dishDto.getCategoryId());
            dishDto.setCategoryName(category.getName());
            dishDtos.add(dishDto);
        }
        return R.success(new PageR<DishDto>(dishDtos,total,pages,pageSize,page));
    }

    //新增套餐
    @Override
    @Transactional
    public void save(SetmealDto setmealDto) {
        setmealDao.insert(setmealDto);
        List<SetmealDish> setmealDishes=setmealDto.getSetmealDishes();
        for(SetmealDish setmealDish:setmealDishes){
            setmealDish.setSetmealId(setmealDto.getId());
            setmealDishService.save(setmealDish);
        }
    }

    @Override
    public R<SetmealDto> getSetmealWithSetmealDishById(Long setmealId) {
        Setmeal setmeal=setmealDao.selectById(setmealId);
        SetmealDto setmealDto=new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        List<SetmealDish> list=setmealDishService.selectBySetmealId(setmealId);
        setmealDto.setSetmealDishes(list);
        return R.success(setmealDto);
    }

    /*删除套餐，只有停售状态才可以删除，否则抛异常*/
    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        //select count(*) from setmeal where id in(..,..) and status=1;
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count=setmealDao.selectCount(queryWrapper);
        if(count>0){
            throw new CustomException("不可删除正在售卖的套餐，请重新选择！");
        }
        //删除setmeal
        setmealDao.deleteBatchIds(ids);
        //用in代替循环删除！删除表setmeal_dish中相关数据
        //delete from setmeal_dish where setmeal_id in (...)f
        setmealDishService.deleteBatchBysetmealIds(ids);
    }

    @Override
    public List<SetmealDto> getListByCategotyId(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus,setmeal.getStatus());
        List<Setmeal> setmeals = setmealDao.selectList(queryWrapper);
        List<SetmealDto> setmealDtos=new ArrayList<>();
        for(Setmeal setmeal1:setmeals){
            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(setmeal1,setmealDto);
            List<SetmealDish> setmealDishes = setmealDishService.selectBySetmealId(setmeal1.getId());
            setmealDto.setSetmealDishes(setmealDishes);
            setmealDtos.add(setmealDto);
        }
        return setmealDtos;
    }
}