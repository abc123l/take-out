package com.reggie.abc.dto;


import com.reggie.abc.entity.Setmeal;
import com.reggie.abc.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
