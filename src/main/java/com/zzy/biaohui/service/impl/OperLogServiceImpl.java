package com.zzy.biaohui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.biaohui.common.Constants;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.exception.BusinessException;
import com.zzy.biaohui.mapper.OperLogMapper;
import com.zzy.biaohui.model.dto.operlog.OperlogQueryRequest;
import com.zzy.biaohui.model.entity.OperLog;
import com.zzy.biaohui.service.OperLogService;
import com.zzy.biaohui.utils.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;

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
        ThrowUtils.throwIf(pageOperLog == null, ErrorCode.PARAMS_ERROR);
        long current = pageOperLog.getCurrent();
        long pageSize = pageOperLog.getPageSize();
        ThrowUtils.throwIf(current <= 0 || pageSize <= 0, ErrorCode.PARAMS_ERROR);
        Long id = pageOperLog.getId();
        String title = pageOperLog.getTitle();
        String businessType = pageOperLog.getBusinessType();
        String operMethod = pageOperLog.getOperMethod();
        String operName = pageOperLog.getOperName();
        String operUrl = pageOperLog.getOperUrl();
        String operIp = pageOperLog.getOperIp();
        String status = pageOperLog.getStatus();
        LocalDateTime startTime = pageOperLog.getStartTime();
        LocalDateTime endTime = pageOperLog.getEndTime();
        String sortField = pageOperLog.getSortField();
        String sortOrder = pageOperLog.getSortOrder();


        LambdaQueryWrapper<OperLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null, OperLog::getId, id);
        queryWrapper.like(StringUtils.isNotBlank(title), OperLog::getTitle, title);
        queryWrapper.eq(StringUtils.isNotBlank(businessType), OperLog::getBusinessType, businessType);
        queryWrapper.eq(StringUtils.isNotBlank(operMethod), OperLog::getOperMethod, operMethod);
        queryWrapper.eq(StringUtils.isNotBlank(operName), OperLog::getOperName, operName);
        queryWrapper.eq(StringUtils.isNotBlank(operUrl), OperLog::getOperUrl, operUrl);
        queryWrapper.eq(StringUtils.isNotBlank(operIp), OperLog::getOperIp, operIp);
        queryWrapper.eq(StringUtils.isNotBlank(status), OperLog::getStatus, status);
        queryWrapper.ge(startTime != null, OperLog::getOperTime, startTime);
        queryWrapper.le(endTime != null, OperLog::getOperTime, endTime);

        if (StringUtils.isNotBlank(sortField)) {
            queryWrapper.orderByDesc(Constants.SORT_ORDER_DESC.equals(sortOrder), OperLog.getLambda(sortField));
        }
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
        ThrowUtils.throwIf(operIp == null, ErrorCode.PARAMS_ERROR);
        if (StringUtils.isAnyBlank(title, businessType, operMethod, operUrl, operIp, operParam)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ThrowUtils.throwIf(StringUtils.isAnyBlank(title, businessType, operMethod, operUrl, operIp, operParam), ErrorCode.PARAMS_ERROR);
        return this.save(operLog);
    }

    @Override
    public boolean deleteOperLog(Long[] ids) {
        ThrowUtils.throwIf(CollectionUtils.isEmpty(Arrays.asList(ids)), ErrorCode.PARAMS_ERROR);
        return this.removeBatchByIds(Arrays.asList(ids));
    }

    @Override
    public OperLog getOperLogById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        return this.getById(id);
    }

    @Override
    public boolean clearOperLog() {
        return this.remove(null);
    }
}




