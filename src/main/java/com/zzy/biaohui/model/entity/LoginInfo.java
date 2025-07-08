package com.zzy.biaohui.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 
 * @TableName login_info
 */
@TableName(value ="login_info")
@Data
public class LoginInfo implements Serializable {

    private static final Map<String, SFunction<LoginInfo, ?>> FIELDS = Map.ofEntries(
            Map.entry("id", LoginInfo::getId),
            Map.entry("userName", LoginInfo::getUserName),
            Map.entry("ipAddr", LoginInfo::getIpAddr),
            Map.entry("loginLocation", LoginInfo::getLoginLocation),
            Map.entry("browser", LoginInfo::getBrowser),
            Map.entry("os", LoginInfo::getOs),
            Map.entry("status", LoginInfo::getStatus),
            Map.entry("msg", LoginInfo::getMsg),
            Map.entry("loginTime", LoginInfo::getLoginTime)
    );

    public static SFunction<LoginInfo, ?> getLambda(String key) {
        return FIELDS.get(key);
    }

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * ip地址
     */
    private String ipAddr;

    /**
     * 登陆地址
     */
    private String loginLocation;

    /**
     * 浏览器信息
     */
    private String browser;

    /**
     * 系统信息
     */
    private String os;

    /**
     * 登陆状态
     */
    private String status;

    /**
     * 
     */
    private String msg;

    /**
     * 访问时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}