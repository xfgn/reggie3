package com.itheima.controller;

import com.itheima.common.R;
import com.itheima.domain.Employee;
import com.itheima.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    //员工登陆
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Map map,HttpServletRequest request){
        /*1.用username查数据库有无该用户
          2.比对密码
          3.查看员工状态是否禁用
          4.登陆成功后将用户Id存入session*/
        R<Employee> result=employeeService.login(map);
        if(result.getData()!=null){
            request.getSession().setAttribute("employee",result.getData().getId());
        }
        return result;
    }
    //退出后台系统
    @PostMapping("/logout")
    public R logout(HttpServletRequest request){
        //清理session中的用户Id
        request.getSession().removeAttribute("employee");
        return R.success(null);
    }
    //添加员工
    @PostMapping
    public R addEmployee(@RequestBody Employee employee){
        return employeeService.addEmployee(employee);
    }

    //分页查询员工信息
    @GetMapping("/page")
    public R Page(Integer page,Integer pageSize,String name){
        return employeeService.page(page,pageSize,name);
    }

    //根据id修改员工信息
    @PutMapping
    public R update(@RequestBody Employee employee){
        return employeeService.updateById(employee);
    }

    //add.html页面加载员工详细信息
    @GetMapping("/{id}")
    public R getEmployeeById(@PathVariable Integer id){
        return employeeService.getEmployeeById(id);
    }
}
