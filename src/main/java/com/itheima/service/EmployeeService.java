package com.itheima.service;

import com.itheima.common.R;
import com.itheima.domain.Employee;

import java.util.Map;

public interface EmployeeService {
    R<Employee> login(Map map);
    R addEmployee(Employee employee);
    R page(Integer page,Integer pageSize,String name);
    R updateById(Employee employee);

    R getEmployeeById(Integer id);
}
