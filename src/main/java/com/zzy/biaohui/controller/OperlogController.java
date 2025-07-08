package com.zzy.biaohui.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.biaohui.common.BaseResponse;
import com.zzy.biaohui.common.DeleteRequest;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.common.ResultUtils;
import com.zzy.biaohui.model.dto.operlog.OperlogQueryRequest;
import com.zzy.biaohui.model.entity.OperLog;
import com.zzy.biaohui.service.OperLogService;
import com.zzy.biaohui.utils.ThrowUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/operlog")
public class OperlogController {

    @Resource
    OperLogService operLogService;

    @GetMapping("/page")
    public BaseResponse<Page<OperLog>> pageOperlog(OperlogQueryRequest operlogQueryRequest) {
        ThrowUtils.throwIf(operlogQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<OperLog> operLogPage = operLogService.pageOperLog(operlogQueryRequest);
        return ResultUtils.success(operLogPage);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteOperlog(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ErrorCode.PARAMS_ERROR);
        Long id = deleteRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        OperLog operLogDB = operLogService.getById(id);
        ThrowUtils.throwIf(operLogDB == null, ErrorCode.OPERATION_ERROR, "要删除的记录不存在");
        boolean b = operLogService.removeById(id);
        return ResultUtils.success(b);
    }

    @GetMapping("/get")
    public BaseResponse<OperLog> getOperlogById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        OperLog operLog = operLogService.getById(id);
        return ResultUtils.success(operLog);
    }

    @PostMapping("/clearAll")
    public BaseResponse<Boolean> clearOperlog() {
        boolean b = operLogService.clearOperLog();
        ThrowUtils.throwIf(!b, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(b);
    }

}
