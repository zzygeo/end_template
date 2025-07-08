package com.zzy.biaohui.interceptor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.common.ResultUtils;
import com.zzy.biaohui.common.ShowTypeCode;
import com.zzy.biaohui.manager.TokenManager;
import com.zzy.biaohui.model.vo.LoginUser;
import com.zzy.biaohui.utils.ServletUtils;
import com.zzy.biaohui.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * 用户验证拦截器
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    TokenManager tokenManager;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            // 如果是预检请求、直接返回
            return true;
        }
        LoginUser loginUser = getUser(request);
        if (loginUser == null || loginUser.getUser() == null) {
            // 这里修改为返回响应包装类
            ObjectMapper objectMapper = new ObjectMapper();
            String s = objectMapper.writeValueAsString(ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, ShowTypeCode.REDIRECT));
            ServletUtils.renderString(response, s);
            return false;
        }
        // 线程上下文
        UserContext.setUser(loginUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 当前请求完成以后释放资源
        UserContext.remove();
    }

    public LoginUser getUser(HttpServletRequest request) {
        // 从redis里获取请求改为从token中解析
        // token解析出来了用户名，从redis里取到用户信息
        // 如果取到用户信息，判断过期时间是否在10分钟以内，如果在10分钟以内，那么就进行续期，并将用户信息存储在usercontext里
        // 如果没有获取到用户，那么返回未认证的错误
        LoginUser loginUser = tokenManager.getLoginUser(request);
        if (loginUser == null) {
            return null;
        }
        // 采用caffeine的话，是通过accessTime自动刷新的，不需要进行续期
//        tokenManager.verifyToken(loginUser);
        return loginUser;
    }
}
