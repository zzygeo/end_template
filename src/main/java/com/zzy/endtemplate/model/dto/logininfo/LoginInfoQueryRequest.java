package com.zzy.endtemplate.model.dto.logininfo;

import com.zzy.endtemplate.common.PageRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author zzy
 * @Description 登陆日志请求类
 */
@Data
public class LoginInfoQueryRequest extends PageRequest {
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
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
