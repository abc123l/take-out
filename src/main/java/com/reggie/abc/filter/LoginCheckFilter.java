package com.reggie.abc.filter;

import com.alibaba.fastjson2.JSON;
import com.reggie.abc.common.BaseContext;
import com.reggie.abc.common.R;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * @author abc
 * @version 1.0
 * 检查用户是否已经登录
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();


        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",//移动端发送短信
                "/user/login"//移动端登录
        };

        boolean check = checkURI(urls, requestURI);




        if (check){
//            log.info("{}请求无需检验是否未登录",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        if (request.getSession().getAttribute("employee")!=null){
            log.info("用户{}已登录",request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);//后面自动填充用 MyMetaObjectHandler

            filterChain.doFilter(request,response);
            return;
        }else if (request.getSession().getAttribute("user")!=null){
            //移动端user
            log.info("用户{}已登录",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);//后面自动填充用

            filterChain.doFilter(request,response);
            return;
        }



        //说明没登录
        log.info("用户未登录,拦截到请求 {}",request.getRequestURI());
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }

    //如果请求路径与放行路径总匹配到则放行
    private boolean checkURI(String[] urls, String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }

}
