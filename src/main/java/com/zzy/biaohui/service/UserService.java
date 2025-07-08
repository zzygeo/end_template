package com.zzy.biaohui.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.biaohui.model.dto.user.UserQueryRequest;
import com.zzy.biaohui.model.entity.User;
import com.zzy.biaohui.model.vo.LoginUser;
import com.zzy.biaohui.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 校验用户名密码
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     */
    void validateUserNamePassword(String userAccount, String userPassword, String checkPassword);

    /**
     * 校验用户名密码
     * @param userAccount
     * @param userPassword
     */
    void validateUserNamePassword(String userAccount, String userPassword);

    /**
     * 添加用户
     * @param user
     * @return
     */
    boolean saveUser(User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    boolean updateUser(User user);

    List<UserVO> listUser(UserQueryRequest userQueryRequest);

    Page<UserVO> pageUser(UserQueryRequest userQueryRequest);
}
