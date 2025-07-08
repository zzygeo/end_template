package com.zzy.biaohui.model.enums;
/**
 * @Author zzy
 * @Description 接口执行成功/失败的状态
 */
public enum BusinessStatus {

    SUCCESS("0", "成功"),
    FAIL("1", "失败");
    
    private String code;
    private String description;
    BusinessStatus(String code, String description) {
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
