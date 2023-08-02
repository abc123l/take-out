package com.reggie.abc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.abc.dto.SetmealDto;
import com.reggie.abc.entity.Setmeal;

import java.util.List;

/**
 * @author abc
 * @version 1.0
 */
public interface SetMealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时保存关联关系
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 修改套餐回显用
     * @param id
     * @return
     */
    SetmealDto getByIdWithDish(Long id);

    void updateWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
