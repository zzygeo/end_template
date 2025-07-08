package com.zzy.biaohui.model.dto.sysfile;

import lombok.Data;

@Data
public class SysFileUpdateRequest {
    private Long id;

    private String fileName;

    private Long menuId;

    private Integer businessType;
}
