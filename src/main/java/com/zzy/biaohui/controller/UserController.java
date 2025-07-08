package com.zzy.biaohui.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.biaohui.annotation.AuthCheck;
import com.zzy.biaohui.annotation.Log;
import com.zzy.biaohui.common.BaseResponse;
import com.zzy.biaohui.common.DeleteRequest;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.common.ResultUtils;
import com.zzy.biaohui.common.constant.UserConstant;
import com.zzy.biaohui.exception.BusinessException;
import com.zzy.biaohui.model.dto.user.*;
import com.zzy.biaohui.model.entity.User;
import com.zzy.biaohui.model.enums.BusinessType;
import com.zzy.biaohui.model.vo.LoginUser;
import com.zzy.biaohui.model.vo.UserVO;
import com.zzy.biaohui.service.UserService;
import com.zzy.biaohui.utils.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户接口
 *
 * @author zzy
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    // region 登录相关

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    @Log(businessType = BusinessType.INSERT, title = "用户模块", excludeParamNames = {"userPassword", "checkPassword"})
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        ThrowUtils.throwIf(StringUtils.equals(userPassword, checkPassword), ErrorCode.PARAMS_ERROR);
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<String> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        ThrowUtils.throwIf(StringUtils.isAnyBlank(userAccount, userPassword), ErrorCode.PARAMS_ERROR);
        String token = userService.userLogin(userAccount, userPassword);
        return ResultUtils.success(token);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUser> getLoginUser() {
        LoginUser loginUser = userService.getLoginUser();
        // 密码脱敏
        loginUser.getUser().setUserPassword(null);
        return ResultUtils.success(loginUser);
    }

    // endregion

    // region 增删改查

    /**
     * 创建用户
     *
     * @param userAddRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 也需要做校验
        boolean result = userService.saveUser(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ErrorCode.PARAMS_ERROR);
        Long id = deleteRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        User userDB = userService.getById(id);
        ThrowUtils.throwIf(userDB == null, ErrorCode.PARAMS_ERROR, "要删除的用户不存在");
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 下沉
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateUser(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取用户
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserVO> getUserById(int id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        // 脱敏
        return ResultUtils.success(userVO);
    }

    /**
     * 获取用户列表
     *
     * @param userQueryRequest
     * @return
     */
    @GetMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<UserVO>> listUser(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<UserVO> userVOList = userService.listUser(userQueryRequest);
        return ResultUtils.success(userVOList);
    }

    /**
     * 分页获取用户列表
     *
     * @param userQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserByPage(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<UserVO> userVOPage = userService.pageUser(userQueryRequest);
        return ResultUtils.success(userVOPage);
    }

    // endregion
}
