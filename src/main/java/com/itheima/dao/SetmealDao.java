package com.itheima.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SetmealDao extends BaseMapper<Setmeal> {
    @Select("select * from setmeal where is_deleted=0 limit #{page},#{pageSize}")
    List<Setmeal> selectPageAll(@Param("page") int page, @Param("pageSize")Integer pageSize);
    @Select("select * from setmeal where is_deleted=0 and name like '%' #{name} '%' limit #{page},#{pageSize}")
    List<Setmeal> selectPageByName(@Param("page")int page, @Param("pageSize")Integer pageSize,@Param("name") String name);
}
