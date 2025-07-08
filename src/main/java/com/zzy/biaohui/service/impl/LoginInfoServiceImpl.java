package com.zzy.biaohui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.biaohui.common.Constants;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.mapper.LoginInfoMapper;
import com.zzy.biaohui.model.dto.logininfo.LoginInfoQueryRequest;
import com.zzy.biaohui.model.entity.LoginInfo;
import com.zzy.biaohui.service.LoginInfoService;
import com.zzy.biaohui.utils.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author zzy
* @description 针对表【login_info】的数据库操作Service实现
* @createDate 2024-09-09 17:49:54
*/
@Service
public class LoginInfoServiceImpl extends ServiceImpl<LoginInfoMapper, LoginInfo>
    implements LoginInfoService{

    @Override
    public Page<LoginInfo> pageLoginFor(LoginInfoQueryRequest loginInfoQueryRequest) {
        ThrowUtils.throwIf(loginInfoQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = loginInfoQueryRequest.getCurrent();
        long pageSize = loginInfoQueryRequest.getPageSize();

        ThrowUtils.throwIf(current <= 0 || pageSize <= 0, ErrorCode.PARAMS_ERROR);
        Long id = loginInfoQueryRequest.getId();
        String userName = loginInfoQueryRequest.getUserName();
        String ipAddr = loginInfoQueryRequest.getIpAddr();
        String loginLocation = loginInfoQueryRequest.getLoginLocation();
        String browser = loginInfoQueryRequest.getBrowser();
        String os = loginInfoQueryRequest.getOs();
        String status = loginInfoQueryRequest.getStatus();
        String msg = loginInfoQueryRequest.getMsg();
        Date startTime = loginInfoQueryRequest.getStartTime();
        Date endTime = loginInfoQueryRequest.getEndTime();
        String sortField = loginInfoQueryRequest.getSortField();
        String sortOrder = loginInfoQueryRequest.getSortOrder();


        LambdaQueryWrapper<LoginInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, LoginInfo::getId, id);
        queryWrapper.like(StringUtils.isNotBlank(userName), LoginInfo::getUserName, userName);
        queryWrapper.like(StringUtils.isNotBlank(ipAddr), LoginInfo::getIpAddr, ipAddr);
        queryWrapper.like(StringUtils.isNotBlank(loginLocation), LoginInfo::getLoginLocation, loginLocation);
        queryWrapper.like(StringUtils.isNotBlank(browser), LoginInfo::getBrowser, browser);
        queryWrapper.like(StringUtils.isNotBlank(os), LoginInfo::getOs, os);
        queryWrapper.eq(StringUtils.isNotBlank(status), LoginInfo::getStatus, status);
        queryWrapper.like(StringUtils.isNotBlank(msg), LoginInfo::getStatus, msg);
        queryWrapper.ge(startTime != null, LoginInfo::getLoginTime, startTime);
        queryWrapper.le(endTime != null, LoginInfo::getLoginTime, endTime);
        if (StringUtils.isNotBlank(sortField)) {
            queryWrapper.orderByDesc(Constants.SORT_ORDER_DESC.equals(sortOrder), LoginInfo.getLambda(sortField));
        }

        Page<LoginInfo> logininforPage = new Page<>(current, pageSize);
        return this.page(logininforPage, queryWrapper);
    }
}




