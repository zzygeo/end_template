package com.zzy.biaohui.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.biaohui.common.BaseResponse;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.common.ResultUtils;
import com.zzy.biaohui.model.dto.logininfo.LoginInfoQueryRequest;
import com.zzy.biaohui.model.entity.LoginInfo;
import com.zzy.biaohui.service.LoginInfoService;
import com.zzy.biaohui.utils.ThrowUtils;
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
        ThrowUtils.throwIf(loginInfoQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<LoginInfo> loginInfoPage = loginInfoService.pageLoginFor(loginInfoQueryRequest);
        return ResultUtils.success(loginInfoPage);
    }
}
