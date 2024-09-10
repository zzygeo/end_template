package com.zzy.endtemplate.constant;

/**
 * 用户常量
 *
 * @author zzy
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_TOKEN_SALT = "userTokenSalt";

    /**
     * token前缀
     */
    String TOKEN_PREFIX = "Bearer ";

     String SALT = "zzynbplus";

    /**
     * 系统用户 id（虚拟用户）
     */
    long SYSTEM_USER_ID = 0;

    //  region 权限

    /**
     * 默认权限
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员权限
     */
    String ADMIN_ROLE = "admin";

    // endregion
}
