package com.reggie.abc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.reggie.abc.entity.OrderDetail;
import com.reggie.abc.mapper.OrderDetailMapper;
import com.reggie.abc.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}