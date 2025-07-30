package com.zzy.biaohui.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.biaohui.common.BaseResponse;
import com.zzy.biaohui.common.DeleteRequest;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.common.ResultUtils;
import com.zzy.biaohui.model.dto.device.DeviceAddRequest;
import com.zzy.biaohui.model.dto.device.DeviceQueryRequest;
import com.zzy.biaohui.model.dto.device.DeviceUpdateRequest;
import com.zzy.biaohui.model.entity.Device;
import com.zzy.biaohui.service.DeviceService;
import com.zzy.biaohui.utils.ThrowUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @PostMapping("/add")
    public BaseResponse<Device> addDevice(@RequestBody DeviceAddRequest deviceAddRequest) {
        ThrowUtils.throwIf(deviceAddRequest == null, ErrorCode.PARAMS_ERROR);
        Device device = deviceService.addDevice(deviceAddRequest);
        return ResultUtils.success(device);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteDevice(@RequestBody DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest == null, ErrorCode.PARAMS_ERROR);
        boolean b = deviceService.deleteDevice(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateDevice(@RequestBody DeviceUpdateRequest deviceUpdateRequest) {
        ThrowUtils.throwIf(deviceUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        boolean b = deviceService.updateDevice(deviceUpdateRequest);
        return ResultUtils.success(b);
    }

    @GetMapping("/page")
    public BaseResponse<Page<Device>> pageDevice(DeviceQueryRequest deviceQueryRequest) {
        ThrowUtils.throwIf(deviceQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<Device> page = deviceService.pageDevice(deviceQueryRequest);
        return ResultUtils.success(page);
    }

    @PostMapping("/importExcel")
    public BaseResponse<Boolean> importExcel(@RequestParam("file") MultipartFile file) {
        ThrowUtils.throwIf(file == null, ErrorCode.PARAMS_ERROR);
        boolean b = deviceService.importExcel(file);
        return ResultUtils.success(b);
    }
}
