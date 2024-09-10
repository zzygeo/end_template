package com.zzy.endtemplate.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.endtemplate.model.entity.User;
import com.zzy.endtemplate.model.vo.LoginUser;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @author zzy
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return token
     */
    String userLogin(String userAccount, String userPassword);

    /**
     * 获取当前登录用户
     *
     * @param
     * @return
     */
    LoginUser getLoginUser();

    /**
     * 是否为管理员
     *
     * @param
     * @return
     */
    boolean isAdmin();

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);
}
