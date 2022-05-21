package com.itheima.service;

import com.itheima.domain.User;

public interface UserService {
    User selectByPhone(String phone);

    void save(User user);

    User selectById(Long userId);
}
