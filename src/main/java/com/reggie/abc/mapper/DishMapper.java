package com.reggie.abc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.abc.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author abc
 * @version 1.0
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
