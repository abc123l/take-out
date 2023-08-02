package com.reggie.abc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.abc.dto.DishDto;
import com.reggie.abc.entity.Dish;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author abc
 * @version 1.0
 */
@Transactional
public interface DishService extends IService<Dish> {
    //新增菜品，同时插入对应的口味
    void saveWithFlavor(DishDto dishDto);
    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}
