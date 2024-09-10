package com.zzy.endtemplate.model.dto.operlog;

import com.zzy.endtemplate.common.PageRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Author zzy
 * @Description 操作日志请求类
 */
@Data
public class OperlogQueryRequest extends PageRequest {

    /**
     * 主键
     */
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
     * 提示消息
     */
    private String msg;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime endTime;
}
