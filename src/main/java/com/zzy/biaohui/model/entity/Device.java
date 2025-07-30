package com.zzy.biaohui.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 设备表
 * @TableName device
 */
@TableName(value ="device")
@Data
@Accessors(chain = true)
public class Device implements Serializable {

    private static final Map<String, SFunction<Device, ?>> FIELDS = Map.ofEntries(
            Map.entry("id", Device::getId),
            Map.entry("deviceId", Device::getDeviceId),
            Map.entry("deviceName", Device::getDeviceName),
            Map.entry("deviceProperty", Device::getDeviceProperty),
            Map.entry("createTime", Device::getCreateTime),
            Map.entry("updateTime", Device::getUpdateTime),
            Map.entry("deviceType", Device::getDeviceType)
    );

    public static SFunction<Device, ?> getLambda(String key) {
        return FIELDS.get(key);
    }

    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 设备分类
     */
    private String deviceType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Device other = (Device) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDeviceId() == null ? other.getDeviceId() == null : this.getDeviceId().equals(other.getDeviceId()))
            && (this.getDeviceName() == null ? other.getDeviceName() == null : this.getDeviceName().equals(other.getDeviceName()))
            && (this.getDeviceProperty() == null ? other.getDeviceProperty() == null : this.getDeviceProperty().equals(other.getDeviceProperty()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDeviceType() == null ? other.getDeviceType() == null : this.getDeviceType().equals(other.getDeviceType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
        result = prime * result + ((getDeviceName() == null) ? 0 : getDeviceName().hashCode());
        result = prime * result + ((getDeviceProperty() == null) ? 0 : getDeviceProperty().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeviceType() == null) ? 0 : getDeviceType().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", deviceName=").append(deviceName);
        sb.append(", deviceProperty=").append(deviceProperty);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deviceType=").append(deviceType);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}