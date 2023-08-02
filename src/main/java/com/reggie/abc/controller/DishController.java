package com.reggie.abc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.reggie.abc.common.R;
import com.reggie.abc.dto.DishDto;
import com.reggie.abc.entity.Category;
import com.reggie.abc.entity.Dish;
import com.reggie.abc.service.CategoryService;
import com.reggie.abc.service.DishFlavorService;
import com.reggie.abc.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author abc
 * @version 1.0
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 页面发来的不仅有菜品信息还有菜品的口味信息，所以要有dishdto对象接收
     * 注意在保存时需要对所有dishFlavor的dishId进行赋值
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 分页返回的数据有一行需要菜品的分类，而Dish只有分类的id，所以需要将page的records换成
     * dishDto并给它添加相应的数据
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //Dish里面只有categoryid,页面上需要categoryName
        Page<Dish> pageInfo=new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.like(name!=null, Dish::getName, name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);


        dishService.page(pageInfo,queryWrapper);

        //对象拷贝，因为Dish中没有categoryName字段，所以要用dishDto对象
        //根据dish的categoryid在category表里面查对应的name并付给对应的dto对象
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();

        //根据records查出对应的categoryName
        List<DishDto> list=records.stream().map((item)->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);//拷贝普通属性

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            //有可能根据categoryId查到的category为空
            if (category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);


        return R.success(dishDtoPage);
    }

    /**
     * 修改菜品做回显用的
     * ，需要菜品信息和口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        dishService.updateWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 添加套餐时候展示菜品用的
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper();
//
//        queryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        queryWrapper.eq(Dish::getStatus,1);
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }

    /**
     * 用户端显示的时候还要显示口味
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper();
        //先根据分类id找到所有的dish
        queryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        queryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(queryWrapper);
        //再把每一个dish根据id找到对应的口味
        List<DishDto> dtoList=list.stream().map(item->{
            DishDto dishDto = dishService.getByIdWithFlavor(item.getId());
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dtoList);
    }

    @PostMapping("/status/{status}")
    public R<String> statusSwitch(@PathVariable("status") int status, Long[] ids){
        for (Long id : ids) {
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }

        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> delete(long[] ids){
        log.info("ids:",ids);
        for (long id : ids) {
            dishService.removeById(id);
        }

        return R.success("删除成功");
    }
}
