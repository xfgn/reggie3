package com.itheima.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.domain.Dish;
import com.itheima.dto.DishDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DishDao extends BaseMapper<Dish> {
    @Select("select d.id as id, d.name as name, d.category_id as categoryId, d.price as price, d.code as code, d.image as image, d.description as description, d.status as status, d.sort as sort, d.create_time as createTime, d.update_time as updateTime,d.create_user as createUser, d.update_user as updateUser,d.is_deleted as isDeleted,c.name as categoryName from dish d inner join category c on d.category_id=c.id limit #{page},#{pageSize}")
    List<DishDto> selectDishDto(@Param("page") Integer page, @Param("pageSize") Integer pageSize);
}
