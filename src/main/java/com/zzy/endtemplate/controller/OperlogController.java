package com.zzy.endtemplate.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.endtemplate.common.BaseResponse;
import com.zzy.endtemplate.common.DeleteRequest;
import com.zzy.endtemplate.common.ErrorCode;
import com.zzy.endtemplate.common.ResultUtils;
import com.zzy.endtemplate.exception.BusinessException;
import com.zzy.endtemplate.model.dto.operlog.OperlogQueryRequest;
import com.zzy.endtemplate.model.entity.OperLog;
import com.zzy.endtemplate.service.OperLogService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/operlog")
public class OperlogController {

    @Resource
    OperLogService operLogService;

    @GetMapping("/page")
    public BaseResponse<Page<OperLog>> pageOperlog(OperlogQueryRequest operlogQueryRequest) {
        if (operlogQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<OperLog> operLogPage = operLogService.pageOperLog(operlogQueryRequest);
        return ResultUtils.success(operLogPage);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteOperlog(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = operLogService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    @GetMapping("/get")
    public BaseResponse<OperLog> getOperlogById(Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        OperLog operLog = operLogService.getById(id);
        return ResultUtils.success(operLog);
    }

    @PostMapping("/clearAll")
    public BaseResponse<Boolean> clearOperlog() {
        boolean b = operLogService.clearOperLog();
        if (!b) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(b);
    }

}
