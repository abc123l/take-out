package com.reggie.abc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.reggie.abc.entity.ShoppingCart;
import com.reggie.abc.mapper.ShoppingCartMapper;
import com.reggie.abc.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
