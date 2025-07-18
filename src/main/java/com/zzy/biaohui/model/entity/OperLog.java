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
 * 日志操作表
 * @TableName oper_log
 */
@TableName(value ="oper_log")
@Data
public class OperLog implements Serializable {

    private static final Map<String, SFunction<OperLog, ?>> FIELDS = Map.ofEntries(
            Map.entry("id", OperLog::getId),
            Map.entry("title", OperLog::getTitle),
            Map.entry("businessType", OperLog::getBusinessType),
            Map.entry("operMethod", OperLog::getOperMethod),
            Map.entry("operName", OperLog::getOperName),
            Map.entry("operUrl", OperLog::getOperUrl),
            Map.entry("operIp", OperLog::getOperIp),
            Map.entry("operParam", OperLog::getOperParam),
            Map.entry("jsonResult", OperLog::getJsonResult),
            Map.entry("status", OperLog::getStatus),
            Map.entry("errorMsg", OperLog::getErrorMsg),
            Map.entry("operTime", OperLog::getOperTime),
            Map.entry("costTime", OperLog::getCostTime)
    );

    public static SFunction<OperLog, ?> getLambda(String fieldName) {
        return FIELDS.get(fieldName);
    }

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
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
     * 请求参数
     */
    private String operParam;

    /**
     * 返回参数
     */
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）
     */
    private String status;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operTime;

    /**
     * 消耗时间
     */
    private Long costTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}