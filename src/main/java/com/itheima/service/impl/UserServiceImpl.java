package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.dao.UserDao;
import com.itheima.domain.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    //根据phone，查找用户是否已注册
    @Override
    public User selectByPhone(String phone) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,phone);
        return userDao.selectOne(queryWrapper);
    }

    //插入新用户
    @Override
    public void save(User user) {
        userDao.insert(user);
    }

    @Override
    public User selectById(Long userId) {
        return userDao.selectById(userId);
    }
}
