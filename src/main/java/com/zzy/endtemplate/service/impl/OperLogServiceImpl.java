package com.zzy.endtemplate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.endtemplate.common.ErrorCode;
import com.zzy.endtemplate.constant.CommonConstant;
import com.zzy.endtemplate.exception.BusinessException;
import com.zzy.endtemplate.mapper.OperLogMapper;
import com.zzy.endtemplate.model.dto.operlog.OperlogQueryRequest;
import com.zzy.endtemplate.model.entity.OperLog;
import com.zzy.endtemplate.service.OperLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
* @author zzy
* @description 针对表【oper_log(日志操作表)】的数据库操作Service实现
* @createDate 2024-09-09 17:48:58
*/
@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog>
    implements OperLogService{

    @Override
    public Page<OperLog> pageOperLog(OperlogQueryRequest pageOperLog) {
        if (pageOperLog == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = pageOperLog.getCurrent();
        long pageSize = pageOperLog.getPageSize();
        if (current <= 0 || pageSize <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = pageOperLog.getId();
        String userName = pageOperLog.getUserName();
        String ipAddr = pageOperLog.getIpAddr();
        String loginLocation = pageOperLog.getLoginLocation();
        String browser = pageOperLog.getBrowser();
        String os = pageOperLog.getOs();
        String status = pageOperLog.getStatus();
        String msg = pageOperLog.getMsg();
        LocalDateTime startTime = pageOperLog.getStartTime();
        LocalDateTime endTime = pageOperLog.getEndTime();
        String sortField = pageOperLog.getSortField();
        String sortOrder = pageOperLog.getSortOrder();

        QueryWrapper<OperLog> queryWrapper = new QueryWrapper<>();
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
        Page<OperLog> loginInfoPage = new Page<>(current, pageSize);
        Page<OperLog> page = this.page(loginInfoPage, queryWrapper);
        return page;
    }

    @Override
    public boolean addOperLog(OperLog operLog) {
        String title = operLog.getTitle();
        String businessType = operLog.getBusinessType();
        String operMethod = operLog.getOperMethod();
        // 如果不登陆就没有操作人
        String operName = operLog.getOperName();
        String operUrl = operLog.getOperUrl();
        String operIp = operLog.getOperIp();
        String operParam = operLog.getOperParam();
        if (operLog == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isAnyBlank(title, businessType, operMethod, operUrl, operIp, operParam)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return this.save(operLog);
    }

    @Override
    public boolean deleteOperLog(Long[] ids) {
        if (CollectionUtils.isEmpty(List.of(ids))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return this.removeBatchByIds(Arrays.asList(ids));
    }

    @Override
    public OperLog getOperLogById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return this.getById(id);
    }

    @Override
    public boolean clearOperLog() {
        return this.remove(null);
    }
}




