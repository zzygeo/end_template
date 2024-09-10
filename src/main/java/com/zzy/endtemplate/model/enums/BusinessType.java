package com.zzy.endtemplate.model.enums;

/**
 * @Author zzy
 * @Description 日志类型
 */

public enum BusinessType {
    /**
     * 其他
     */
    OTHER("0", "其他"),
    /**
     * 新增
     */
    INSERT("1", "新增"),
    /**
     * 修改
     */
    UPDATE("2", "修改"),
    /**
     * 删除
     */
    DELETE("3", "删除"),
    /**
     * 授权
     */
    GRANT("4", "授权"),
    /**
     * 导出
     */
    EXPORT("5", "导出"),
    /**
     * 导入
     */
    IMPORT("6", "导入"),
    /**
     * 清理
     */
    CLEAN("7", "清理");
    
    private String code;
    private String description;
    BusinessType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
