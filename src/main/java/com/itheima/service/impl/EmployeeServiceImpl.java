package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.R;
import com.itheima.dao.EmployeeDao;
import com.itheima.domain.Employee;
import com.itheima.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;
    @Override
    public R<Employee> login(Map map) {
        String password=(String) map.get("password");
        password= DigestUtils.md5DigestAsHex(password.getBytes());//md5加密
        String username=(String) map.get("username");
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("username",username);
        Employee employee=employeeDao.selectOne(wrapper);
        if(employee==null){
            return R.error("用户名不存在！");
        }
        if(!password.equals(employee.getPassword())){
            return R.error("密码错误!");
        }
        if(employee.getStatus()==0){
            return R.error("该用户已被禁用!");
        }
        return R.success(employee);
    }

    @Override
    public R addEmployee(Employee employee) {
        System.out.println(LocalDateTime.now());
       /* employee.setCreateTime(LocalDateTime.now());
        employee.setCreateUser(id);
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(id);*/
        employee.setPassword("123456");
        int count=employeeDao.insert(employee);
        if(count!=1){
            return R.error("添加员工失败！");
        }
        return R.success(null);
    }

    @Override
    public R<Page> page(Integer currentPage,Integer pageSize,String name) {
        Page<Employee> page= new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Employee::getName,name);
        page=employeeDao.selectPage(page,queryWrapper);
        return R.success(page);
    }

    @Override
    public R updateById(Employee employee) {
        /*employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(id);*/
        employeeDao.updateById(employee);
        return R.success(null);
    }

    @Override
    public R getEmployeeById(Integer id) {
        Employee employee=employeeDao.selectById(id);
        return R.success(employee);
    }
}
