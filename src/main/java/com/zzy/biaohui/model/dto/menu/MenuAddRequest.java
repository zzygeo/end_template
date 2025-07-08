package com.zzy.biaohui.model.dto.menu;

import lombok.Data;

@Data
public class MenuAddRequest {
    private Long parentId;

    private String name;

    private String iconUrl;
}
