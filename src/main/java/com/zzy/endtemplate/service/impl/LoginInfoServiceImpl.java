package com.zzy.endtemplate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.endtemplate.common.ErrorCode;
import com.zzy.endtemplate.constant.CommonConstant;
import com.zzy.endtemplate.exception.BusinessException;
import com.zzy.endtemplate.mapper.LoginInfoMapper;
import com.zzy.endtemplate.model.dto.logininfo.LoginInfoQueryRequest;
import com.zzy.endtemplate.model.entity.LoginInfo;
import com.zzy.endtemplate.service.LoginInfoService;
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
        if (loginInfoQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = loginInfoQueryRequest.getCurrent();
        long pageSize = loginInfoQueryRequest.getPageSize();
        if (current <=  0 || pageSize <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
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


        QueryWrapper<LoginInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StringUtils.isNotBlank(ipAddr), "ipAddr", ipAddr);
        queryWrapper.like(StringUtils.isNotBlank(loginLocation), "loginLocation", loginLocation);
        queryWrapper.like(StringUtils.isNotBlank(browser), "browser", browser);
        queryWrapper.like(StringUtils.isNotBlank(os), "os", os);
        queryWrapper.eq(StringUtils.isNotBlank(status), "status", status);
        queryWrapper.like(StringUtils.isNotBlank(msg), "msg", msg);
        queryWrapper.ge(startTime != null, "startTime", startTime);
        queryWrapper.le(endTime != null, "endTime", endTime);
        queryWrapper.orderByDesc(CommonConstant.SORT_ORDER_DESC.equals(sortOrder), sortField);

        Page<LoginInfo> logininforPage = new Page<>(current, pageSize);
        return this.page(logininforPage, queryWrapper);
    }
}




