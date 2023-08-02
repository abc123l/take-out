package com.reggie.abc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.abc.entity.Employee;
import com.reggie.abc.mapper.EmployeeMapper;
import com.reggie.abc.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author abc
 * @version 1.0
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{

}
