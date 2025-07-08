package com.zzy.biaohui.aop;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zzy.biaohui.annotation.AuthCheck;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.exception.BusinessException;
import com.zzy.biaohui.model.entity.User;
import com.zzy.biaohui.model.vo.LoginUser;
import com.zzy.biaohui.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 权限校验 AOP
 *
 * @author zzy
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行拦截
     *
     * @param joinPoint
     * @param authCheck
     * @return
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        List<String> anyRole = Arrays.stream(authCheck.anyRole()).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        String mustRole = authCheck.mustRole();
        // 当前登录用户
        LoginUser loginUser = userService.getLoginUser();
        User user = loginUser.getUser();
        // 拥有任意权限即通过
        if (CollectionUtils.isNotEmpty(anyRole)) {
            String userRole = user.getUserRole();
            if (!anyRole.contains(userRole)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        // 必须有所有权限才通过
        if (StringUtils.isNotBlank(mustRole)) {
            String userRole = user.getUserRole();
            if (!mustRole.equals(userRole)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
            }
        }
        // 通过权限校验，放行
        return joinPoint.proceed();
    }
}

