package com.reggie.abc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.reggie.abc.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author abc
 * @version 1.0
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
