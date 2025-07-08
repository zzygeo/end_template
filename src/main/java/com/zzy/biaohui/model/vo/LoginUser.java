package com.zzy.biaohui.model.vo;

import com.zzy.biaohui.model.entity.User;
import lombok.Data;

@Data
public class LoginUser {
    private User user;
    /**
     * uuid，当前生成令牌的随机uuid值
     */
    private String uuid;
    /**
     * 登陆时间
     */
    private Long loginTime;
    /**
     * 过期时间
     */
    private Long expireTime;
    /**
     * 登录ip地址
     */
    private String ipaddr;
}
