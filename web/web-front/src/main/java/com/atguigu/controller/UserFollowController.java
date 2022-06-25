package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.UserInfo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.atguigu.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/userFollow")
public class UserFollowController extends BaseController {

    @Reference
    UserFollowService userFollowService;


    //取消关注
    @GetMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable Long id ,HttpServletRequest request){
        userFollowService.delete(id);
        return Result.ok();
    }


    //我的关注列表
    @GetMapping(value = "/auth/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable Integer pageNum, @PathVariable Integer pageSize, HttpServletRequest request) {
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");
        Long userId = userInfo.getId();
        PageInfo<UserFollowVo> pageInfo = userFollowService.findListPage(pageNum, pageSize, userId);
        return Result.ok(pageInfo);
    }


    //添加关注
    @GetMapping("/auth/follow/{id}")
    public Result follow(@PathVariable Long id, HttpServletRequest request){
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("USER");

        userFollowService.follow(userInfo.getId(),id);

        return Result.ok();
    }

}
