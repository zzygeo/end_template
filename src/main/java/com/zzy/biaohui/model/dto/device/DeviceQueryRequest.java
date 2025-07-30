package com.zzy.biaohui.model.dto.device;

import com.zzy.biaohui.common.PageRequest;
import lombok.Data;

@Data
public class DeviceQueryRequest extends PageRequest {
    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备分类
     */
    private String deviceType;
}
