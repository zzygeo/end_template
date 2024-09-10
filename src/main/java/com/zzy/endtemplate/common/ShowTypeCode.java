package com.zzy.endtemplate.common;

import lombok.Getter;

/**
 * @Author zzy
 * @Description 返回给前端的提示
 */

@Getter
public enum ShowTypeCode {
    SILENT(0, "不提示"),
    WARN_MESSAGE(1, "警告消息"),
    ERROR_MESSAGE(2, "错误警告"),
    NOTIFICATION(3, "提示消息"),
    REDIRECT(9, "重定向");

    private int code;
    private String message;
    ShowTypeCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
