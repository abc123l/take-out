package com.reggie.abc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggie.abc.common.BaseContext;
import com.reggie.abc.common.R;
import com.reggie.abc.entity.ShoppingCart;
import com.reggie.abc.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author abc
 * @version 1.0
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        shoppingCart.setUserId(BaseContext.getCurrentId());
        //查询当前菜品或套餐是否已经在购物车中，如果在的话直接在number字段+1
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        if (dishId!=null){
            //说明要加入的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            //说明要加入的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }
        //查询该用户下的菜品或套餐是否为空
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        if (one!=null){
            //该用户的该菜品已存在
            one.setNumber(one.getNumber()+1);
            shoppingCartService.updateById(one);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one=shoppingCart;
        }
        return R.success(one);

    }

    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){

        shoppingCart.setUserId(BaseContext.getCurrentId());
        //查询当前菜品或套餐是否已经在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        if (dishId!=null){
            //说明要删除的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            //说明要删除的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }
        //查询的结果一定不为空
        ShoppingCart toBeSubbed = shoppingCartService.getOne(queryWrapper);
        if (toBeSubbed.getNumber()==1){
            shoppingCartService.removeById(toBeSubbed);
        }else {
            toBeSubbed.setNumber(toBeSubbed.getNumber()-1);
            shoppingCartService.updateById(toBeSubbed);
        }
        return R.success("修改成功");

    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }

}
