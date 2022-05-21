package com.itheima.controller;

import com.itheima.common.R;
import com.itheima.domain.AddressBook;
import com.itheima.domain.User;
import com.itheima.service.AddressBookService;
import com.itheima.utils.BaseContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    //新增收货地址
    @PostMapping
    public R saveAddress(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContextUtil.get());
        addressBookService.save(addressBook);
        return R.success(null);
    }

    //修改收货地址
    @PutMapping
    public R updateAddress(@RequestBody AddressBook addressBook){
        addressBookService.updateAddressById(addressBook);
        return R.success(null);
    }


    //设为默认地址,只能有一个
    @PutMapping("/default")
    public R setDefaultAddress(@RequestBody AddressBook addressBook){
        //需要先把之前的默认地址清空后再设置新的默认地址
        addressBook.setUserId(BaseContextUtil.get());
        addressBookService.setDefaultAddress(addressBook);
        return R.success(null);
    }

    //查询默认地址
    @GetMapping("/default")
    public R<AddressBook> getDefaultAddress(){
        //根据账号id查
        AddressBook addressBook=addressBookService.getDefaultAddress();
        if(addressBook!=null){
            return R.success(addressBook);
        }else{
            return R.error("未设置默认地址");
        }
    }

    //查询指定用户的全部地址
    @GetMapping("/list")
    public R<List<AddressBook>> getAll(){
        return addressBookService.getAllAddressByUserId(BaseContextUtil.get());
    }

    //查询单个地址详细信息
    @GetMapping("/{id}")
    public R<AddressBook> getAddressById(@PathVariable long id){
        return R.success(addressBookService.selectAddressById(id));
    }
}
