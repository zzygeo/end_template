package com.zzy.biaohui.common.constant;

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

     String USER_PASSWORD_ENCRYPT_SALT = "zzynbplus";

    /**
     * 系统用户 id（虚拟用户）
     */
    long ADMIN_ID = 1;

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
