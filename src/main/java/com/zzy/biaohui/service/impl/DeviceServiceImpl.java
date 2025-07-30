package com.zzy.biaohui.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.biaohui.common.Constants;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.exception.BusinessException;
import com.zzy.biaohui.manager.easyexcel.DeviceExcelListener;
import com.zzy.biaohui.mapper.DeviceMapper;
import com.zzy.biaohui.model.dto.device.DeviceAddRequest;
import com.zzy.biaohui.model.dto.device.DeviceExcelImport;
import com.zzy.biaohui.model.dto.device.DeviceQueryRequest;
import com.zzy.biaohui.model.dto.device.DeviceUpdateRequest;
import com.zzy.biaohui.model.entity.Device;
import com.zzy.biaohui.service.DeviceService;
import com.zzy.biaohui.utils.ThrowUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
* @author zzy
* @description 针对表【device(设备表)】的数据库操作Service实现
* @createDate 2025-07-24 09:58:34
*/
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device>
    implements DeviceService {

    @Override
    public Device addDevice(DeviceAddRequest deviceAddRequest) {
        ThrowUtils.throwIf(deviceAddRequest == null, ErrorCode.PARAMS_ERROR);
        String deviceId = deviceAddRequest.getDeviceId();
        String deviceName = deviceAddRequest.getDeviceName();
        ThrowUtils.throwIf(StrUtil.isBlank(deviceId), ErrorCode.PARAMS_ERROR);
        if (StrUtil.isBlank(deviceName)) {
            deviceAddRequest.setDeviceName("默认名称");
        }
        Device device = new Device();
        BeanUtils.copyProperties(deviceAddRequest, device);
        boolean save = this.save(device);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "更新失败");
        return device;
    }

    @Override
    public Page<Device> pageDevice(DeviceQueryRequest deviceQueryRequest) {
        ThrowUtils.throwIf(deviceQueryRequest == null, ErrorCode.PARAMS_ERROR);
        String deviceId = deviceQueryRequest.getDeviceId();
        String deviceName = deviceQueryRequest.getDeviceName();
        String deviceType = deviceQueryRequest.getDeviceType();
        long current = deviceQueryRequest.getCurrent();
        long pageSize = deviceQueryRequest.getPageSize();
        String sortField = deviceQueryRequest.getSortField();
        String sortOrder = deviceQueryRequest.getSortOrder();

        LambdaQueryWrapper<Device> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(deviceId), Device::getDeviceId, deviceId);
        queryWrapper.like(StrUtil.isNotBlank(deviceName), Device::getDeviceName, deviceName);
        queryWrapper.eq(StrUtil.isNotBlank(deviceType), Device::getDeviceType, deviceType);
        if (StrUtil.isNotBlank(sortField)) {
            queryWrapper.orderByDesc(Constants.SORT_ORDER_DESC.equals(sortOrder), Device.getLambda(sortField));
            queryWrapper.orderByAsc(Constants.SORT_ORDER_ASC.equals(sortOrder), Device.getLambda(sortField));
        }
        Page<Device> page = this.page(new Page<>(current, pageSize), queryWrapper);
        return page;
    }

    @Override
    public boolean deleteDevice(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        Device oldDevice = this.getById(id);
        ThrowUtils.throwIf(oldDevice == null, ErrorCode.PARAMS_ERROR, "设备不存在");
        boolean remove = this.removeById(id);
        ThrowUtils.throwIf(!remove, ErrorCode.OPERATION_ERROR, "删除失败");
        return true;
    }

    @Override
    public boolean updateDevice(DeviceUpdateRequest deviceUpdateRequest) {
        ThrowUtils.throwIf(deviceUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        Integer id = deviceUpdateRequest.getId();
        String deviceId = deviceUpdateRequest.getDeviceId();
        String deviceName = deviceUpdateRequest.getDeviceName();
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        Device oldDevice = this.getById(id);
        ThrowUtils.throwIf(oldDevice == null, ErrorCode.PARAMS_ERROR, "设备不存在");
        ThrowUtils.throwIf(StrUtil.isNotBlank(deviceId) && deviceId.length() > 50, ErrorCode.PARAMS_ERROR, "id过长");
        ThrowUtils.throwIf(StrUtil.isNotBlank(deviceName) && deviceName.length() > 50, ErrorCode.PARAMS_ERROR, "名称过长");

        Device device = new Device();
        BeanUtils.copyProperties(deviceUpdateRequest, device);
        boolean update = this.updateById(device);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR, "更新失败");
        return true;
    }

    @Override
    public Device getDeviceById(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        return this.getById(id);
    }

    @Override
    public boolean importExcel(MultipartFile file) {
        ThrowUtils.throwIf(file == null || file.isEmpty(), ErrorCode.PARAMS_ERROR);
        try {
            EasyExcel.read(file.getInputStream(), DeviceExcelImport.class, new DeviceExcelListener()).sheet().doRead();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
        return true;
    }
}




