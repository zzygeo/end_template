package com.zzy.biaohui.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 用户
 *
 * @TableName user
 */
@TableName(value = "user")
@Data
public class User implements Serializable {

    private static final Map<String, SFunction<User, ?>> FIELDS = Map.of("id", User::getId, "userName", User::getUserName, "userAccount", User::getUserAccount, "userAvatar", User::getUserAvatar, "gender", User::getGender, "userRole", User::getUserRole, "userPassword", User::getUserPassword, "createTime", User::getCreateTime, "updateTime", User::getUpdateTime, "isDelete", User::getIsDelete);

    public static SFunction<User, ?> getLambda(String fieldName) {
        return FIELDS.get(fieldName);
    }

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}