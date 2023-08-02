package com.reggie.abc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.reggie.abc.entity.AddressBook;
import com.reggie.abc.mapper.AddressBookMapper;
import com.reggie.abc.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
