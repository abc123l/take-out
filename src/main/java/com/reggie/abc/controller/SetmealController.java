package com.reggie.abc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.abc.common.R;
import com.reggie.abc.dto.SetmealDto;
import com.reggie.abc.entity.Category;
import com.reggie.abc.entity.Setmeal;
import com.reggie.abc.service.CategoryService;
import com.reggie.abc.service.SetMealService;
import com.reggie.abc.service.SetmealDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author abc
 * @version 1.0
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @CacheEvict(value = "setMealCache", allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setMealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setMealService.page(pageInfo, queryWrapper);//pageInfo里面有了数据


        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> lists = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(lists);

        return R.success(dtoPage);
    }

    /**
     * 修改套餐回显用
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> show(@PathVariable Long id){
        return R.success(setMealService.getByIdWithDish(id));
    }

    @PutMapping
    @CacheEvict(value = "setMealCache", allEntries = true)
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setMealService.updateWithDish(setmealDto);
        return R.success("修改套餐成功");
    }

    /**
     * 修改状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    @CacheEvict(value = "setMealCache", allEntries = true)
    public R<String> statusSwitch(@PathVariable("status") int status, Long[] ids){
        for (Long id : ids) {
            Setmeal setmeal = setMealService.getById(id);
            setmeal.setStatus(status);
            setMealService.updateById(setmeal);
        }
        return R.success("修改成功");
    }

    @DeleteMapping
    @CacheEvict(value = "setMealCache", allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids){
        setMealService.removeWithDish(ids);
        return R.success("套餐删除成功");
    }

    @GetMapping("/list")
    @Cacheable(value = "setMealCache",key = "#setmeal.categoryId")
    public R<List<Setmeal>> list(Setmeal setmeal){

        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setMealService.list(queryWrapper);
        return R.success(list);
    }


}
