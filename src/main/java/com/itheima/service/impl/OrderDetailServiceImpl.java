package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.dao.OrderDetailDao;
import com.itheima.domain.OrderDetail;
import com.itheima.domain.ShoppingCart;
import com.itheima.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailDao orderDetailDao;


    @Override
    public void insertBatch(List<OrderDetail> orderDetails) {
        for(OrderDetail orderDetail:orderDetails){
            orderDetailDao.insert(orderDetail);
        }
    }

    //根据orderId查询订单细节
    @Override
    public List<OrderDetail> selectByOrderId(Long id) {
        LambdaQueryWrapper<OrderDetail> query=new LambdaQueryWrapper<>();
        query.eq(OrderDetail::getOrderId,id);
        return orderDetailDao.selectList(query);
    }
}
