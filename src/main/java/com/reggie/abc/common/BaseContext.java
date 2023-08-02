package com.reggie.abc.common;

/**
 * @author abc
 * @version 1.0
 * 基于threadlocal获得登录对象的id在字段自动填充时用
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal=new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
