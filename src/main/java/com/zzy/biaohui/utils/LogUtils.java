package com.zzy.biaohui.utils;

/**
 * @Author zzy
 * @Description 日志格式化工具
 */

public class LogUtils {
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
