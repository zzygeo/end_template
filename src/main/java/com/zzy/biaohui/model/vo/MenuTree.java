package com.zzy.biaohui.model.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MenuTree {
    private Long id;

    /**
     * 父菜单id
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单图标
     */
    private String iconUrl;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     * 子节点
     */
    private List<MenuTree> children;
}
