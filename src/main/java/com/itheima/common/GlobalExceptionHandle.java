package com.itheima.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandle {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R SQLException(SQLIntegrityConstraintViolationException ex){
        ex.printStackTrace();
        if(ex.getMessage().contains("Duplicate entry")){
            String[] str=ex.getMessage().split(" ");
            String msg=str[2]+"已存在！";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
    @ExceptionHandler(CustomException.class)
    public R doException(CustomException ex){
        ex.printStackTrace();
        return R.error(ex.getMessage());
    }
}
