package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import com.atguigu.vo.RegisterVo;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Reference
    UserInfoService userInfoService;

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
