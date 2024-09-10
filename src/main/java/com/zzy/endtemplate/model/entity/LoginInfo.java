package com.zzy.endtemplate.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName login_info
 */
@TableName(value ="login_info")
@Data
public class LoginInfo implements Serializable {
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
    private Date loginTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}