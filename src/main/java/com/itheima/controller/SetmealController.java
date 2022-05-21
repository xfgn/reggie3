package com.itheima.controller;

import com.itheima.common.PageR;
import com.itheima.common.R;
import com.itheima.domain.Setmeal;
import com.itheima.dto.SetmealDto;
import com.itheima.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    //分页查询
    @GetMapping("/page")
    public R<PageR> page(Integer page, Integer pageSize, String name){
        return setmealService.page(page,pageSize,name);
    }

    //新增套餐setmeal
    @PostMapping
    public R save(@RequestBody SetmealDto setmealDto){
        //操作两个表
        setmealService.save(setmealDto);
        return R.success(null);
    }

    //套餐具体信息回显
    @GetMapping("/{id}")
    public R<SetmealDto> edit(@PathVariable Long id){
        return setmealService.getSetmealWithSetmealDishById(id);
    }

    //售卖中的套餐不能删除，停售后才可以删掉
    /*这里加注解@RequestParam是因为http传来的参数以逗号隔开，默认为数组形式，而我以list接收，导致名称ids虽然匹配
    * 但类型不匹配，也无法完成转换，使用注解可实现强制转换*/
    @DeleteMapping
    public R delete(@RequestParam List<Long> ids){
        log.debug("ids,{}",ids);
        setmealService.deleteByIds(ids);
        return R.success(null);
    }

    //根据categoryId查询套餐菜品列表
    @GetMapping("/list")
    public R<List<SetmealDto>> getListByCategoryId(Setmeal setmeal){
        return R.success(setmealService.getListByCategotyId(setmeal));
    }
}
