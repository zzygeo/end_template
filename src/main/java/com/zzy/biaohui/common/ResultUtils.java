package com.zzy.biaohui.common;

/**
 * 返回工具类
 *
 * @author zzy
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, 0, "", ShowTypeCode.SILENT.getCode(), data);
    }

    /**
     * 错误消息、默认提示
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<Void>(errorCode, ShowTypeCode.ERROR_MESSAGE);
    }

    /**
     * 自定义
     * @param code
     * @param message
     * @param showTypeCode
     * @return
     */
    public static BaseResponse error(int code, String message, ShowTypeCode showTypeCode) {
        return new BaseResponse<Void>(false, code, message, showTypeCode.getCode(), null);
    }

    /**
     * 错误提示、自定义消息
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String message) {
        return new BaseResponse<Void>(false, errorCode.getCode(), message, ShowTypeCode.ERROR_MESSAGE.getCode(), null);
    }

    /**
     * 自定义提示、默认消息
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, ShowTypeCode showTypeCode) {
        return new BaseResponse<Void>(false, errorCode.getCode(), errorCode.getMessage(), showTypeCode.getCode(), null);
    }

    /**
     * 自定义
     * @param errorCode
     * @param message
     * @param showTypeCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String message, ShowTypeCode showTypeCode) {
        return new BaseResponse<Void>(false, errorCode.getCode(), message, showTypeCode.getCode(), null);
    }
}
