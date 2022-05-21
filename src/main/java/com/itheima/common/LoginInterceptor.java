package com.itheima.common;

import com.alibaba.fastjson.JSON;
import com.itheima.utils.BaseContextUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //后台登录验证
        Long emp_id =(Long) request.getSession().getAttribute("employee");
        if(emp_id!=null){
            BaseContextUtil.set(emp_id);
            return true;
        }

        //会员端登录验证
        Long user_id =(Long) request.getSession().getAttribute("user");
        if(user_id!=null){//成功，放行
            BaseContextUtil.set(user_id);
            return true;
        }

        //登录不成功，返回NOTLOGIN，前端仍有拦截器，可实现登录页面的跳转
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return false;
    }
}
