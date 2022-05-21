package com.itheima.controller;

import com.itheima.common.R;
import com.itheima.domain.User;
import com.itheima.service.UserService;
import com.itheima.utils.BaseContextUtil;
import com.itheima.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;

    //获取验证码--发邮件
    @PostMapping("/sendMsg")
    public R sendMsg(@RequestBody User user, HttpSession session){
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        log.info("code={},",code);
        //发邮件
       /* try{
            SimpleMailMessage mailMessage=new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(user.getPhone());
            mailMessage.setSubject("瑞吉外卖验证码");
            mailMessage.setText("[瑞吉外卖]您的验证码是"+code+",5分钟内有效，请勿泄露给他人");
            javaMailSender.send(mailMessage);
        }catch (Exception exception){
            exception.printStackTrace();
            return R.error("邮件发送失败！");
        }*/
        session.setAttribute(user.getPhone().substring(0,11),code);
        return R.success(null);
    }

    //移动端用户登录
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String,String> map, HttpSession session){
        //验证验证码是否正确
        Object codeInSession = session.getAttribute(map.get("phone"));
        if(codeInSession!=null && codeInSession.toString().equals(map.get("code"))){
            //核查phone是否存在，若不存在自动注册为新用户
            User user=userService.selectByPhone(map.get("phone"));
            if(user==null){
                user=new User();
                user.setName(map.get("phone")+"用户123");
                user.setPhone(map.get("phone"));
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }

        return R.error("验证码错误！");
    }

    //获取登录用户信息
    @GetMapping("/getUser")
    public R<User> getUser(){
        User user = userService.selectById(BaseContextUtil.get());
        return R.success(user);
    }

    //退出登录
    @PostMapping("/loginout")
    public R loginOut(HttpSession session){
        session.removeAttribute("user");
        return R.success(null);
    }
}
