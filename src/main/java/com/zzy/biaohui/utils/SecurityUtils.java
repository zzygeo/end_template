package com.zzy.biaohui.utils;

import com.zzy.biaohui.common.constant.UserConstant;
import org.springframework.util.DigestUtils;

/**
 * @Author zzy
 * @Description 安全认证相关
 */

public class SecurityUtils {

    /**
     * 密码加密
     * @param password
     * @return
     */
    public static String encryptPassword(String password) {
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.USER_PASSWORD_ENCRYPT_SALT + password).getBytes());
        return encryptPassword;
    }

    /**
     *
     * 密码是否匹配
     * @param rawPassword
     * @param encodedPassword
     * @return
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        String encryptPassword = SecurityUtils.encryptPassword(rawPassword);
        return encryptPassword.equals(encodedPassword);
    }
}
