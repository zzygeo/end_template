package com.zzy.endtemplate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.endtemplate.common.Constants;
import com.zzy.endtemplate.common.ErrorCode;
import com.zzy.endtemplate.common.ShowTypeCode;
import com.zzy.endtemplate.exception.BusinessException;
import com.zzy.endtemplate.manager.AsyncManager;
import com.zzy.endtemplate.manager.TokenManager;
import com.zzy.endtemplate.manager.factory.AsyncFactory;
import com.zzy.endtemplate.mapper.UserMapper;
import com.zzy.endtemplate.model.entity.User;
import com.zzy.endtemplate.model.vo.LoginUser;
import com.zzy.endtemplate.service.UserService;
import com.zzy.endtemplate.utils.SecurityUtils;
import com.zzy.endtemplate.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.zzy.endtemplate.constant.UserConstant.ADMIN_ROLE;


/**
 * 用户服务实现类
 *
 * @author zzy
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenManager tokenManager;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = userMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = SecurityUtils.encryptPassword(userPassword);
            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public String userLogin(String userAccount, String userPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = SecurityUtils.encryptPassword(userPassword);
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态，并创建token
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);
        // 记录登陆日志
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(user.getUserAccount(), Constants.LOGIN_SUCCESS, "user.login.success"));
        // 更新账户的登陆时间
        String token = tokenManager.createToken(loginUser);
        return token;
    }


    /**
     * 获取当前登录用户
     *
     * @param
     * @return
     */
    @Override
    public LoginUser getLoginUser() {
        // 先判断是否已登录
        LoginUser loginUser = UserContext.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, ShowTypeCode.REDIRECT);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = loginUser.getUser().getId();
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, ShowTypeCode.REDIRECT);
        }
        loginUser.setUser(user);
        return loginUser;
    }

    /**
     * 是否为管理员
     *
     * @param
     * @return
     */
    @Override
    public boolean isAdmin() {
        // 仅管理员可查询
        LoginUser loginUser = UserContext.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            return false;
        }
        User user = loginUser.getUser();
        return user != null && ADMIN_ROLE.equals(user.getUserRole());
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        LoginUser loginUser = tokenManager.getLoginUser(request);
        if (loginUser == null || loginUser.getUser() == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        tokenManager.delLoginUser(loginUser.getUuid());
        // todo 日志用户登出
        return true;
    }
}




