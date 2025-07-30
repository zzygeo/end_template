package com.zzy.biaohui.model.dto.device;

import lombok.Data;

@Data
public class DeviceAddRequest {

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备属性
     */
    private String deviceProperty;

    /**
     * 设备分类
     */
    private String deviceType;
}
