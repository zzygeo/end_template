package com.zzy.biaohui.model.dto.operlog;

import com.zzy.biaohui.common.PageRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Author zzy
 * @Description 操作日志请求类
 */
@Data
public class OperlogQueryRequest extends PageRequest {

    private Long id;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 业务类型（0其他 1新增 2修改 3删除）
     */
    private String businessType;

    /**
     * 方法名称
     */
    private String operMethod;

    /**
     * 操作人员
     */
    private String operName;

    /**
     * 请求URL
     */
    private String operUrl;

    /**
     * 请求ip
     */
    private String operIp;

    /**
     * 操作状态（0正常 1异常）
     */
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime endTime;
}
