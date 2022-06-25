package com.atguigu.interceptor;

import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.util.WebUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object userInfo = request.getSession().getAttribute("USER");
        if (StringUtils.isEmpty(userInfo)) {
            Result result = Result.build("未登录", ResultCodeEnum.LOGIN_AUTH);
            //xxxStr = JSON.toJSONString(result)
            //response.getWriter().print(xxxStr);
            WebUtil.writeJSON(response,result); //将数据转换为json返回给客户端浏览器
            return false;
        }
        return true;
    }
}
