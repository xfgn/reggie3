package com.itheima.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.PageR;
import com.itheima.common.R;
import com.itheima.domain.Orders;
import com.itheima.dto.OrdersDto;
import com.itheima.service.OrderService;
import com.itheima.utils.BaseContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //点击去支付,完成付款
    @PostMapping("/submit")
    public R submit(@RequestBody Orders order){
        order.setUserId(BaseContextUtil.get());
        orderService.submit(order);
        return R.success("支付成功");
    }

    //个人页面--获取最新订单
    @GetMapping("/userPage")
    public R<Page<OrdersDto>> getLatestOrder(int page, int pageSize){
        return orderService.getLatestOrder(page,pageSize);
    }


}
