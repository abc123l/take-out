package com.reggie.abc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.reggie.abc.common.R;
import com.reggie.abc.entity.User;
import com.reggie.abc.service.UserService;
import com.reggie.abc.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author abc
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone= user.getPhone();
        //生成随机4位验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        //调用短信api发送短信
//        SMSUtils.sendMessage();
        //将生成的验证码保存到session
//        session.setAttribute(phone,code);

        //将生成的验证码放到redis当中，并设置有效期为5min
        redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);

        log.info("手机号{}生成的验证码为{}",phone,code);
        return R.success("手机验证码发送成功");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        //获取手机号，和验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        //在session中获取该手机号对应的验证码
//        Object codeInSession = session.getAttribute(phone);
        //从redis中取验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);
        //比对
        if (codeInSession!=null && codeInSession.equals(code)){
            //判断手机号是否在user表里面，如果没有就保存下来
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user==null){
                user=new User();
                user.setPhone(phone);
                userService.save(user);
            }

            session.setAttribute("user",user.getId());
            //如果用户登录成功，删除redis中的手机号验证码
            redisTemplate.delete(phone);
            return R.success(user);
        }
        return R.error("登陆失败，用户填写的验证码与系统session中该手机的验证码不一致");
    }

    //用户登出
    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request){
        //清理Session中保存的当前用户登录的id
        request.getSession().removeAttribute("user");
        return R.success("用户退出登录");
    }
}
