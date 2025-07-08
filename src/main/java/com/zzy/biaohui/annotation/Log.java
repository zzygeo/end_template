package com.zzy.biaohui.annotation;

import com.zzy.biaohui.model.enums.BusinessType;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 模块
     *
     * @return
     */
    public String title() default "";

    /**
     * 功能
     *
     * @return
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * 是否保存响应的参数
     *
     * @return
     */
    public boolean isSaveResponseData() default true;

    /**
     * 是否保存请求的参数
     *
     * @return
     */
    public boolean isSaveRequestData() default true;

    /**
     * 排除指定的请求参数
     * @return
     */
    public String[] excludeParamNames() default {};
}
