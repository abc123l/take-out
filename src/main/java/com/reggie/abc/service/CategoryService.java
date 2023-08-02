package com.reggie.abc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.abc.entity.Category;

/**
 * @author abc
 * @version 1.0
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
