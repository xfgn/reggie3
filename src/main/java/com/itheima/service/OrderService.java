package com.itheima.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.PageR;
import com.itheima.common.R;
import com.itheima.domain.Orders;
import com.itheima.dto.OrdersDto;

public interface OrderService {
    void submit(Orders order);

    R<Page<OrdersDto>> getLatestOrder(int page, int pageSize);
}
