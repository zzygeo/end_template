package com.zzy.biaohui.utils;


import com.zzy.biaohui.model.vo.LoginUser;

/**
 * @Author zzy
 * @Description 用户上下文
 */

public class UserContext {
    private final static ThreadLocal<LoginUser> threadLocal = new ThreadLocal<>();

    public static LoginUser getLoginUser() {
        return threadLocal.get();
    }

    public static void setUser(LoginUser loginUser) {
        threadLocal.set(loginUser);
    }

    public static void remove() {
        threadLocal.remove();
    }
}
