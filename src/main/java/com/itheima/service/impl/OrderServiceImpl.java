package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.common.CustomException;
import com.itheima.common.PageR;
import com.itheima.common.R;
import com.itheima.dao.OrderDao;
import com.itheima.domain.*;
import com.itheima.dto.OrdersDto;
import com.itheima.service.*;
import com.itheima.utils.BaseContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private UserService userService;

    //生成订单
    @Override
    @Transactional
    public void submit(Orders order) {
        //查询当前用户id购物车数据
        List<ShoppingCart> cartList = shoppingCartService.getCartListByUserId(order.getUserId());
        if(cartList==null||cartList.size()==0){
            throw new CustomException("用户购物车为空，不能下单");
        }
        //原子类，保证线程安全
        AtomicInteger amount=new AtomicInteger(0);
        for(ShoppingCart cart:cartList){
            //金额*份数
            amount.addAndGet(cart.getAmount().multiply(new BigDecimal(cart.getNumber())).intValue());
        }
        //查地址详细信息，根据adressId
        AddressBook addressBook=addressBookService.selectAddressById(order.getAddressBookId());
        if(addressBook==null){
            throw new CustomException("用户地址有误，不能下单");
        }
        //查询用户数据
        User user=userService.selectById(order.getUserId());
        //向订单表插入数据，一条
        order.setStatus(2);
        order.setNumber(String.valueOf(System.currentTimeMillis()));//订单号
        order.setOrderTime(LocalDateTime.now());//下单时间
        order.setAmount(new BigDecimal(amount.get()));//实收金额
        order.setUserName(user.getName());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        order.setPhone(addressBook.getPhone());
        order.setCheckoutTime(LocalDateTime.now());//付款时间
        orderDao.insert(order);
        //向订单明细表插入数据，多条
        List<OrderDetail> orderDetails=new ArrayList<>();
        for(ShoppingCart cart:cartList){
            OrderDetail orderDetail=new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetails.add(orderDetail);
        }
        orderDetailService.insertBatch(orderDetails);
        //清空购物车数据
        shoppingCartService.cleanShoppingCart(order.getUserId());
    }

    //分页获取最新订单
    @Override
    public R<Page<OrdersDto>> getLatestOrder(int page, int pageSize) {
        //根据userId分页查order信息
        LambdaQueryWrapper<Orders> query=new LambdaQueryWrapper<>();
        query.eq(Orders::getUserId, BaseContextUtil.get());
        query.orderByDesc(Orders::getCheckoutTime);
        IPage<Orders> iPage=new Page<>(page,pageSize);
        orderDao.selectPage(iPage, query);
        //查orderDetail
        Page<OrdersDto> dtoIPage=new Page<>();
        BeanUtils.copyProperties(iPage,dtoIPage,"records");
        List<OrdersDto> ordersDtoList=new ArrayList<>();
        List<Orders> records = iPage.getRecords();
        for(Orders orders:records){
            List<OrderDetail> orderDetails = orderDetailService.selectByOrderId(orders.getId());
            OrdersDto ordersDto=new OrdersDto();
            BeanUtils.copyProperties(orders,ordersDto);
            ordersDto.setOrderDetails(orderDetails);
            ordersDtoList.add(ordersDto);
        }
        dtoIPage.setRecords(ordersDtoList);
        return R.success(dtoIPage);
    }
}
