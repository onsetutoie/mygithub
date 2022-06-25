package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import com.atguigu.vo.LoginVo;
import com.atguigu.vo.RegisterVo;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Reference
    UserInfoService userInfoService;

    //退出登录
    @GetMapping("/logout")
    public Result logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        if (session != null) {
            session.removeAttribute("USER");
        }
        return Result.ok();

    }

    // 登录 校验
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo,HttpServletRequest request){
        String password = loginVo.getPassword();
        String phone = loginVo.getPhone();

        //校验参数
        if(StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }

        //校验账号
        UserInfo userInfo = userInfoService.getByPhone(phone);
        if(null == userInfo) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR);
        }

        //校验密码
        if(!MD5.encrypt(password).equals(userInfo.getPassword())) {
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
        }

        //校验是否被禁用
        if(userInfo.getStatus() == 0) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }

        request.getSession().setAttribute("USER",userInfo);

        Map<String, Object> map = new HashMap<>();
        map.put("phone",phone);
        map.put("nickName",userInfo.getNickName());

        return Result.ok(map);

    }

    // 注册 校验
    @PostMapping("/register")
    public Result register(@RequestBody RegisterVo registerVo , HttpServletRequest request){
        String phone = registerVo.getPhone();
        String password = registerVo.getPassword();
        String nickName = registerVo.getNickName();
        String code = registerVo.getCode();

        //校验参数
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password) || StringUtils.isEmpty(nickName) || StringUtils.isEmpty(code)){
                return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }

        //验证码
        String codeTest = (String) request.getSession().getAttribute("code");
        if (!codeTest.equals(code)) {
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }

        //验证手机号是否注册
        UserInfo userInfo = userInfoService.getByPhone(phone);
        if (userInfo != null ) {
            return Result.build(null,ResultCodeEnum.PHONE_REGISTER_ERROR);
        }

        userInfo = new UserInfo();
        BeanUtils.copyProperties(registerVo,userInfo);
        userInfo.setStatus(1);
        userInfo.setPassword(MD5.encrypt(password));
        userInfoService.insert(userInfo);

        return Result.ok();


    }

    @RequestMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable String phone, HttpServletRequest request){
        String code = "1111";
        request.getSession().setAttribute("code",code);
        return Result.ok(code);

    }
}
