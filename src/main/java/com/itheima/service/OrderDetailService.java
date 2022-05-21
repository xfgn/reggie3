package com.itheima.service;

import com.itheima.domain.OrderDetail;
import com.itheima.domain.ShoppingCart;

import java.util.List;

public interface OrderDetailService {

    void insertBatch(List<OrderDetail> orderDetails);

    List<OrderDetail> selectByOrderId(Long id);
}
