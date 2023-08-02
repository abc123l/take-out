package com.reggie.abc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.abc.common.R;
import com.reggie.abc.dto.DishDto;
import com.reggie.abc.entity.Dish;
import com.reggie.abc.entity.DishFlavor;
import com.reggie.abc.mapper.DishMapper;
import com.reggie.abc.service.DishFlavorService;
import com.reggie.abc.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author abc
 * @version 1.0
 */
@Service
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //将菜品信息保存到Dish表
        this.save(dishDto);//dishdto时dish的子类
        Long dishId = dishDto.getId();

        //要为flavor的dishId赋值
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map(item->{
           item.setDishId(dishId);
           return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 修改菜品时做回显用的
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        DishDto dishDto=new DishDto();
        Dish dish = this.getById(id);
        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    /**
     * 这里和添加不一样的是口味可能会发生记录的删除和新增
     * @param dishDto
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新菜品表
        this.updateById(dishDto);

        //清理当前菜品对应的口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //添加新的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors=flavors.stream().map(item->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());


        dishFlavorService.saveBatch(flavors);
    }


}
