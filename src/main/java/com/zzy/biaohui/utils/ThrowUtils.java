package com.zzy.biaohui.utils;

import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.common.ShowTypeCode;
import com.zzy.biaohui.exception.BusinessException;

public class ThrowUtils {

    /**
     * 错误消息
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        if (condition) {
            throw new BusinessException(errorCode, message);
        }
    }

    /**
     * 默认错误消息
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        if (condition) {
            throw new BusinessException(errorCode);
        }
    }

    /**
     * 自定义提示类型
     * @param condition
     * @param errorCode
     * @param showTypeCode
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, ShowTypeCode showTypeCode) {
        if (condition) {
            throw new BusinessException(errorCode, showTypeCode);
        }
    }

    /**
     * 自定义提示类型、自定义消息
     * @param condition
     * @param errorCode
     * @param message
     * @param showTypeCode
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message, ShowTypeCode showTypeCode) {
        if (condition) {
            throw new BusinessException(errorCode, message, showTypeCode);
        }
    }
}
