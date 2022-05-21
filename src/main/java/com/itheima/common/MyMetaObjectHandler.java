package com.itheima.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.itheima.utils.BaseContextUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /*执行流程：
    拦截器-->controller的update()/insert()方法-->updatefill()/insertfill()
    */
    @Override
    public void insertFill(MetaObject metaObject) {
        /*metaObject传入的是含@TableField的整体对象
        * setValue()方法，第一个参数为属性名，第二个为字段自动填充值*/
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContextUtil.get());
        metaObject.setValue("updateUser", BaseContextUtil.get());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContextUtil.get());
    }
}
