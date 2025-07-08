package com.zzy.biaohui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.biaohui.common.Constants;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.exception.BusinessException;
import com.zzy.biaohui.manager.AsyncManager;
import com.zzy.biaohui.manager.TokenManager;
import com.zzy.biaohui.manager.factory.AsyncFactory;
import com.zzy.biaohui.mapper.UserMapper;
import com.zzy.biaohui.model.dto.user.UserQueryRequest;
import com.zzy.biaohui.model.entity.User;
import com.zzy.biaohui.model.vo.LoginUser;
import com.zzy.biaohui.model.vo.UserVO;
import com.zzy.biaohui.service.UserService;
import com.zzy.biaohui.utils.SecurityUtils;
import com.zzy.biaohui.utils.ThrowUtils;
import com.zzy.biaohui.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.zzy.biaohui.common.constant.UserConstant.ADMIN_ROLE;


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
        ThrowUtils.throwIf(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword), ErrorCode.PARAMS_ERROR);
        validateUserNamePassword(userAccount, userPassword, checkPassword);
        synchronized (userAccount.intern()) {
            // 账户不能重复
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserAccount, userAccount);
            long count = userMapper.selectCount(queryWrapper);
            ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "账号重复");
            // 2. 加密
            String encryptPassword = SecurityUtils.encryptPassword(userPassword);
            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            ThrowUtils.throwIf(!saveResult, ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            return user.getId();
        }
    }

    @Override
    public String userLogin(String userAccount, String userPassword) {
        // 1. 校验
        ThrowUtils.throwIf(StringUtils.isAnyBlank(userAccount, userPassword), ErrorCode.PARAMS_ERROR);
        validateUserNamePassword(userAccount, userPassword);
        // 2. 加密
        String encryptPassword = SecurityUtils.encryptPassword(userPassword);
        // 查询用户是否存在

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        queryWrapper.eq(User::getUserPassword, encryptPassword);
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

        ThrowUtils.throwIf(loginUser == null || loginUser.getUser() == null, ErrorCode.NOT_LOGIN_ERROR);
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = loginUser.getUser().getId();
        User user = this.getById(userId);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_LOGIN_ERROR);
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
        ThrowUtils.throwIf(loginUser == null || loginUser.getUser() == null, ErrorCode.NOT_LOGIN_ERROR);
        // 移除登录态
        tokenManager.delLoginUser(loginUser.getUuid());
        // todo 日志用户登出
        return true;
    }

    @Override
    public boolean saveUser(User user) {
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR);
        String userAccount = user.getUserAccount();
        String userPassword = user.getUserPassword();
        ThrowUtils.throwIf(StringUtils.isAnyBlank(userAccount, userPassword), ErrorCode.PARAMS_ERROR);
        validateUserNamePassword(userAccount, userPassword);
        String encryptPassword = SecurityUtils.encryptPassword(userPassword);
        user.setUserPassword(encryptPassword);
        return this.save(user);
    }

    @Override
    public boolean updateUser(User user) {
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR);
        Long userId = user.getId();
        String userAccount = user.getUserAccount();
        String userPassword = user.getUserPassword();
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf("admin".equals(userAccount), ErrorCode.PARAMS_ERROR, "不能对admin进行修改");

        if (StringUtils.isNotBlank(userAccount) && user.getUserAccount().length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (StringUtils.isNotBlank(userPassword) && user.getUserPassword().length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 用户不存在
        User userDB = this.getById(userId);
        ThrowUtils.throwIf(userDB == null, ErrorCode.PARAMS_ERROR, "用户不存在");
        if (StringUtils.isNotBlank(userPassword)) {
            String encryptPassword = SecurityUtils.encryptPassword(userPassword);
            user.setUserPassword(encryptPassword);
        }
        boolean b = this.updateById(user);
        if (b) {
            // 更新成功，修改缓存
            User newUser = this.getById(userId);
            LoginUser loginUser = new LoginUser();
            loginUser.setUser(newUser);
            tokenManager.refreshToken(loginUser);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<UserVO> listUser(UserQueryRequest userQueryRequest) {
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        String userAvatar = userQueryRequest.getUserAvatar();
        Integer gender = userQueryRequest.getGender();
        String userRole = userQueryRequest.getUserRole();
        Date startTimes = userQueryRequest.getStartTimes();
        Date endTimes = userQueryRequest.getEndTimes();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, User::getId, id);
        queryWrapper.like(StringUtils.isNotBlank(userName), User::getUserName, userName);
        queryWrapper.like(StringUtils.isNotBlank(userAccount), User::getUserAccount, userAccount);
        queryWrapper.like(StringUtils.isNotBlank(userAvatar), User::getUserAvatar, userAvatar);
        queryWrapper.eq(gender != null, User::getGender, gender);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), User::getUserRole, userRole);
        queryWrapper.ge(startTimes != null, User::getUpdateTime, startTimes);
        queryWrapper.le(endTimes != null, User::getUpdateTime, endTimes);
        if (StringUtils.isNotBlank(sortField)) {
            // 如果传入降序，则为true, 则按字段的降序排序，
            queryWrapper.orderByDesc(Constants.SORT_ORDER_ASC.equals(sortOrder), User.getLambda(sortField));
        }
        List<User> userList = this.list(queryWrapper);
        List<UserVO> userVOList = userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        return userVOList;
    }

    @Override
    public Page<UserVO> pageUser(UserQueryRequest userQueryRequest) {
        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        ThrowUtils.throwIf(current <= 0 || pageSize <= 0, ErrorCode.PARAMS_ERROR);
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        String userAvatar = userQueryRequest.getUserAvatar();
        Integer gender = userQueryRequest.getGender();
        String userRole = userQueryRequest.getUserRole();
        Date startTimes = userQueryRequest.getStartTimes();
        Date endTimes = userQueryRequest.getEndTimes();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, User::getId, id);
        queryWrapper.like(StringUtils.isNotBlank(userName), User::getUserName, userName);
        queryWrapper.like(StringUtils.isNotBlank(userAccount), User::getUserAccount, userAccount);
        queryWrapper.like(StringUtils.isNotBlank(userAvatar), User::getUserAvatar, userAvatar);
        queryWrapper.eq(gender != null, User::getGender, gender);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), User::getUserRole, userRole);
        queryWrapper.ge(startTimes != null, User::getUpdateTime, startTimes);
        queryWrapper.le(endTimes != null, User::getUpdateTime, endTimes);
        if (StringUtils.isNotBlank(sortField)) {
            // 如果传入降序，则为true, 则按字段的降序排序，
            queryWrapper.orderByDesc(Constants.SORT_ORDER_DESC.equals(sortOrder), User.getLambda(sortField));
        }
        Page<User> userPage = this.page(new Page<>(current, pageSize), queryWrapper);
        Page<UserVO> userVOPage = new PageDTO<>(current, pageSize, userPage.getTotal());
        List<UserVO> userVOList = userPage.getRecords().stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        userVOPage.setRecords(userVOList);
        return userVOPage;
    }

    public void validateUserNamePassword(String userAccount, String userPassword, String checkPassword) {
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
    }

    public void validateUserNamePassword(String userAccount, String userPassword) {
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
    }
}




