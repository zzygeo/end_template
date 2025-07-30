package com.zzy.biaohui.manager.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.exception.BusinessException;
import com.zzy.biaohui.model.dto.device.DeviceExcelImport;
import com.zzy.biaohui.model.entity.Device;
import com.zzy.biaohui.service.DeviceService;
import com.zzy.biaohui.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class DeviceExcelListener implements ReadListener<DeviceExcelImport> {
    public static final int MAX_BATCH_COUNT = 1000;
    private List<DeviceExcelImport> list = new ArrayList<>(MAX_BATCH_COUNT);

    // 读取每行数据触发
    @Override
    public void invoke(DeviceExcelImport data, AnalysisContext context) {
        if (list.size() >= MAX_BATCH_COUNT) {
            // 存储数据
            saveData();
            // 清空数据
            list = new ArrayList<>(MAX_BATCH_COUNT);
        } else {
            list.add(data);
        }
    }

    // 读取完毕触发
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 存储最后一批数据
        saveData();
    }

    private void saveData() {
        DeviceService deviceService = SpringUtils.getBean(DeviceService.class);
        List<Device> devices = list.stream().map(deviceExcelImport -> {
            String deviceId = deviceExcelImport.getDeviceId();
            String deviceName = deviceExcelImport.getDeviceName();
            String deviceProperty = deviceExcelImport.getDeviceProperty();
            Device device = new Device();
            device.setDeviceId(deviceId).setDeviceName(deviceName).setDeviceProperty(deviceProperty);
            return device;
        }).collect(Collectors.toList());
        boolean success = deviceService.saveBatch(devices);
        log.info("{}条数据，存储数据成功!", list.size());
        if (!success) {
            // todo 记录错误行
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "导入失败");
        }
    }
}
