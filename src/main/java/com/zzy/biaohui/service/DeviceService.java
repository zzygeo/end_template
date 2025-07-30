package com.zzy.biaohui.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.biaohui.model.dto.device.DeviceAddRequest;
import com.zzy.biaohui.model.dto.device.DeviceQueryRequest;
import com.zzy.biaohui.model.dto.device.DeviceUpdateRequest;
import com.zzy.biaohui.model.entity.Device;
import org.springframework.web.multipart.MultipartFile;

/**
* @author zzy
* @description 针对表【device(设备表)】的数据库操作Service
* @createDate 2025-07-24 09:58:34
*/
public interface DeviceService extends IService<Device> {
    // 添加设备
    Device addDevice(DeviceAddRequest deviceAddRequest);
    // 分页查询设备
    Page<Device> pageDevice(DeviceQueryRequest deviceQueryRequest);
    // 删除设备
    boolean deleteDevice(Long id);
    // 更新设备
    boolean updateDevice(DeviceUpdateRequest deviceUpdateRequest);
    // 根据id查询设备
    Device getDeviceById(Long id);
    // 导入excel数据
    boolean importExcel(MultipartFile file);
}
