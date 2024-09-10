package com.zzy.endtemplate.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.endtemplate.common.BaseResponse;
import com.zzy.endtemplate.common.ErrorCode;
import com.zzy.endtemplate.common.ResultUtils;
import com.zzy.endtemplate.exception.BusinessException;
import com.zzy.endtemplate.model.dto.logininfo.LoginInfoQueryRequest;
import com.zzy.endtemplate.model.entity.LoginInfo;
import com.zzy.endtemplate.service.LoginInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/logininfo")
public class LoginInfoController {
    @Resource
    private LoginInfoService loginInfoService;

    @GetMapping("/page")
    public BaseResponse<Page<LoginInfo>> pageLoginInfo(LoginInfoQueryRequest loginInfoQueryRequest) {
        if (loginInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<LoginInfo> loginInfoPage = loginInfoService.pageLoginFor(loginInfoQueryRequest);
        return ResultUtils.success(loginInfoPage);
    }
}
