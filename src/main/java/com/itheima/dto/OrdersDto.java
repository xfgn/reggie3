package com.itheima.dto;

import com.itheima.domain.OrderDetail;
import com.itheima.domain.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private List<OrderDetail> orderDetails;
	
}
