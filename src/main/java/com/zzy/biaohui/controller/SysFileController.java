package com.zzy.biaohui.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.biaohui.common.BaseResponse;
import com.zzy.biaohui.common.DeleteRequest;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.common.ResultUtils;
import com.zzy.biaohui.model.dto.sysfile.SysFileQueryRequest;
import com.zzy.biaohui.model.dto.sysfile.SysFileUpdateRequest;
import com.zzy.biaohui.model.dto.sysfile.SysFileUploadRequest;
import com.zzy.biaohui.model.dto.sysfile.ThumbnailUploadRequest;
import com.zzy.biaohui.model.entity.SysFile;
import com.zzy.biaohui.model.enums.FileBusinessType;
import com.zzy.biaohui.model.vo.LabelValue;
import com.zzy.biaohui.service.SysFileService;
import com.zzy.biaohui.utils.ThrowUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/file")
public class SysFileController {
    @Autowired
    private SysFileService sysFileService;

    @PostMapping("/upload")
    public BaseResponse<SysFile> uploadSysFile(SysFileUploadRequest sysFileUploadRequest) {
        ThrowUtils.throwIf(sysFileUploadRequest == null, ErrorCode.PARAMS_ERROR);
        SysFile sysFile = sysFileService.addSysFile(sysFileUploadRequest);
        ThrowUtils.throwIf(sysFile == null, ErrorCode.SYSTEM_ERROR, "上传文件失败");
        return ResultUtils.success(sysFile);
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

    @GetMapping("/list")
    public BaseResponse<List<SysFile>> listSysFile(SysFileQueryRequest sysFileQueryRequest) {
        ThrowUtils.throwIf(sysFileQueryRequest == null, ErrorCode.PARAMS_ERROR);
        List<SysFile> sysFiles = sysFileService.listSysFile(sysFileQueryRequest);
        return ResultUtils.success(sysFiles);
    }

    @GetMapping("/page")
    public BaseResponse<Page<SysFile>> pageSysFile(SysFileQueryRequest sysFileQueryRequest) {
        ThrowUtils.throwIf(sysFileQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<SysFile> sysFiles = sysFileService.pageSysFile(sysFileQueryRequest);
        return ResultUtils.success(sysFiles);
    }

    @GetMapping("/businessType")
    public BaseResponse<List<LabelValue>> listFileBusinessType() {
        List<LabelValue> values = FileBusinessType.getValues();
        return ResultUtils.success(values);
    }

    @PostMapping("/uploadThumbnail")
    public BaseResponse<Boolean> uploadThumbnail(ThumbnailUploadRequest thumbnailUploadRequest) {
        ThrowUtils.throwIf(thumbnailUploadRequest == null, ErrorCode.PARAMS_ERROR);
        boolean b = sysFileService.uploadThumbnail(thumbnailUploadRequest);
        ThrowUtils.throwIf(!b, ErrorCode.SYSTEM_ERROR, "上传缩略图失败");
        return ResultUtils.success(true);
    }

    @PostMapping("/batchDelete")
    public BaseResponse<Boolean> batchDelete(@RequestBody List<Long> ids) {
        boolean b = sysFileService.batchDelete(ids);
        return ResultUtils.success(b);
    }
}
