package com.reggie.abc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.abc.common.CustomException;
import com.reggie.abc.entity.Category;
import com.reggie.abc.entity.Dish;
import com.reggie.abc.entity.Setmeal;
import com.reggie.abc.mapper.CategoryMapper;
import com.reggie.abc.service.CategoryService;
import com.reggie.abc.service.DishService;
import com.reggie.abc.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author abc
 * @version 1.0
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetMealService setMealService;

    @Override
    public void remove(Long id) {
        //判断要删除的分类是不是关联了菜品或者套餐
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, id);
        long count = dishService.count(queryWrapper);
        if (count > 0) {
            throw new CustomException("当前分类关联了菜品，不能删除");
        }


        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId, id);
        long count1 = setMealService.count(queryWrapper1);
        if (count1 > 0) {
            throw new CustomException("当前分类关联了套餐，不能删除");
        }

        //说明未关联其它数据
        super.removeById(id);
    }
}
