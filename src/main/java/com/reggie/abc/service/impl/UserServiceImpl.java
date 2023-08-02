package com.reggie.abc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.reggie.abc.entity.User;
import com.reggie.abc.mapper.UserMapper;
import com.reggie.abc.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
