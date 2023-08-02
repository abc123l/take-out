package com.reggie.abc.dto;


import com.reggie.abc.entity.Dish;
import com.reggie.abc.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * 添加菜品时，页面返回的还有口味，所以不能用Dish接收
 */
@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
