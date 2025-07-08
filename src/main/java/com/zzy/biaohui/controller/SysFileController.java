package com.zzy.biaohui.controller;

import com.zzy.biaohui.common.BaseResponse;
import com.zzy.biaohui.common.DeleteRequest;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.common.ResultUtils;
import com.zzy.biaohui.model.dto.sysfile.SysFileQueryRequest;
import com.zzy.biaohui.model.dto.sysfile.SysFileUpdateRequest;
import com.zzy.biaohui.model.dto.sysfile.SysFileUploadRequest;
import com.zzy.biaohui.model.entity.SysFile;
import com.zzy.biaohui.service.SysFileService;
import com.zzy.biaohui.utils.ThrowUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/file")
public class SysFileController {
    @Autowired
    private SysFileService sysFileService;

    @PostMapping("/upload")
    public BaseResponse<Boolean> uploadSysFile(SysFileUploadRequest sysFileUploadRequest) {
        ThrowUtils.throwIf(sysFileUploadRequest == null, ErrorCode.PARAMS_ERROR);
        boolean b = sysFileService.addSysFile(sysFileUploadRequest);
        ThrowUtils.throwIf(!b, ErrorCode.SYSTEM_ERROR, "上传文件失败");
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateSysFile(@RequestBody SysFileUpdateRequest sysFileUpdateRequest) {
        ThrowUtils.throwIf(sysFileUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        boolean b = sysFileService.updateSysFile(sysFileUpdateRequest);
        ThrowUtils.throwIf(!b, ErrorCode.SYSTEM_ERROR, "更新文件失败");
        return ResultUtils.success(true);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteSysFile(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ErrorCode.PARAMS_ERROR);
        Long id = deleteRequest.getId();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        boolean b = sysFileService.deleteSysFile(id);
        ThrowUtils.throwIf(!b, ErrorCode.SYSTEM_ERROR, "删除文件失败");
        return ResultUtils.success(true);
    }

    @PostMapping("/list")
    public BaseResponse<List<SysFile>> listSysFile(SysFileQueryRequest sysFileQueryRequest) {
        ThrowUtils.throwIf(sysFileQueryRequest == null, ErrorCode.PARAMS_ERROR);
        List<SysFile> sysFiles = sysFileService.listSysFile(sysFileQueryRequest);
        return ResultUtils.success(sysFiles);
    }
}
