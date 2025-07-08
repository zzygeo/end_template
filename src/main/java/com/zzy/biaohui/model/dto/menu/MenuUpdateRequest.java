package com.zzy.biaohui.model.dto.menu;

import lombok.Data;

@Data
public class MenuUpdateRequest {
    private Long id;

    private String parentId;

    private String name;

    private String iconUrl;
}
