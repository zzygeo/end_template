package com.zzy.biaohui.model.dto.device;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DeviceExcelImport {
    @ExcelProperty(value = "设备ID")
    private String deviceId;

    @ExcelProperty(value = "设备名称")
    private String deviceName;

    @ExcelProperty(value = "设备属性")
    private String deviceProperty;
}
