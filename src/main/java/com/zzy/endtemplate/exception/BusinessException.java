package com.zzy.endtemplate.exception;

import com.zzy.endtemplate.common.ErrorCode;
import com.zzy.endtemplate.common.ShowTypeCode;

/**
 * 自定义异常类
 *
 * @author zzy
 */
public class BusinessException extends RuntimeException {

    private final int code;
    private final ShowTypeCode showTypeCode;

    public BusinessException(int code, String message, ShowTypeCode showTypeCode) {
        super(message);
        this.code = code;
        this.showTypeCode = showTypeCode;
    }

    /**
     * 错误提示，默认消息
     * @param errorCode 业务异常状态
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.showTypeCode = ShowTypeCode.ERROR_MESSAGE;
    }

    /**
     * 自定义提示、默认消息
     * @param errorCode 业务异常状态类型
     * @param showTypeCode 提示消息类型
     */
    public BusinessException(ErrorCode errorCode, ShowTypeCode showTypeCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.showTypeCode = showTypeCode;
    }

    /**
     * 错误提示、自定义消息
     * @param errorCode 业务异常状态类型
     * @param message 提示消息
     */
    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
        this.showTypeCode = ShowTypeCode.ERROR_MESSAGE;
    }

    /**
     * 完全自主
     * @param errorCode 义务异常类型
     * @param message 错误消息
     * @param showTypeCode 提示类型
     */
    public BusinessException(ErrorCode errorCode, String message, ShowTypeCode showTypeCode) {
        super(message);
        this.code = errorCode.getCode();
        this.showTypeCode = showTypeCode;
    }

    public int getCode() {
        return code;
    }

    public ShowTypeCode getShowTypeCode() {return showTypeCode;}
}
