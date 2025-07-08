package com.zzy.biaohui.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 * @author zzy
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -1066383262602414468L;
    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误code
     */
    private int errorCode;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 消息类型
     */
    private int showType;

    /**
     * 数据
     */
    private T data;

    public BaseResponse(boolean success, int errorCode, String errorMessage,
                        int showType, T data) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.showType = showType;
        this.data = data;
    }

    public BaseResponse() {}

    /**
     * 成功的消息
     * @param data
     */
    public BaseResponse(T data) {
        this(true, 0, "", com.zzy.biaohui.common.ShowTypeCode.SILENT.getCode(), data);
    }

    /**
     * 错误的消息
     * @param errorCode
     * @param showType
     */
    public BaseResponse(ErrorCode errorCode, com.zzy.biaohui.common.ShowTypeCode showType) {
        this(false, errorCode.getCode(), errorCode.getMessage(), showType.getCode(),null);
    }
}
