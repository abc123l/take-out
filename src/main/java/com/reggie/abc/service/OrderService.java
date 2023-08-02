package com.reggie.abc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.abc.entity.Orders;


public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
