package com.itheima.service;

import com.itheima.common.R;
import com.itheima.domain.AddressBook;

import java.util.List;

public interface AddressBookService {
    void save(AddressBook addressBook);

    void updateAddressById(AddressBook addressBook);

    void setDefaultAddress(AddressBook addressBook);

    AddressBook getDefaultAddress();

    R<List<AddressBook>> getAllAddressByUserId(Long aLong);

    AddressBook selectAddressById(long id);
}
