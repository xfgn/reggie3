package com.itheima.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.common.R;
import com.itheima.dao.AddressBookDao;
import com.itheima.domain.AddressBook;
import com.itheima.service.AddressBookService;
import com.itheima.utils.BaseContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookDao addressBookDao;

    //新增收货地址
    @Override
    public void save(AddressBook addressBook) {
        addressBookDao.insert(addressBook);
    }

    //修改收货地址
    @Override
    public void updateAddressById(AddressBook addressBook) {
        addressBookDao.updateById(addressBook);
    }

    //设置默认收货地址
    @Override
    @Transactional
    public void setDefaultAddress(AddressBook addressBook) {
        //把该账号下的默认地址清空
        //update from address_book set isDefault=0 where user_id=?;
        LambdaQueryWrapper<AddressBook> query=new LambdaQueryWrapper<>();
        addressBook.setIsDefault(0);
        query.eq(AddressBook::getUserId,addressBook.getUserId());
        addressBookDao.update(addressBook,query);

        //设置新的默认地址，update from address_book set isDefault=1 where id=?;
        addressBook.setIsDefault(1);
        addressBookDao.updateById(addressBook);
    }

    //查询默认收货地址
    @Override
    public AddressBook getDefaultAddress() {
        LambdaQueryWrapper<AddressBook> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContextUtil.get());
        queryWrapper.eq(AddressBook::getIsDefault,1);
        return addressBookDao.selectOne(queryWrapper);
    }

    //获取指定账号下所有地址信息
    @Override
    public R<List<AddressBook>> getAllAddressByUserId(Long userId) {
        LambdaQueryWrapper<AddressBook> query=new LambdaQueryWrapper<>();
        query.eq(userId!=null,AddressBook::getUserId,userId);
        query.orderByDesc(AddressBook::getUpdateTime);
        return R.success(addressBookDao.selectList(query));
    }

    @Override
    public AddressBook selectAddressById(long id) {
        return addressBookDao.selectById(id);
    }
}
