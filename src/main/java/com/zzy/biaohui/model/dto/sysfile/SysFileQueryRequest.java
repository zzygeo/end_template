package com.zzy.biaohui.model.dto.sysfile;

import com.zzy.biaohui.common.PageRequest;
import lombok.Data;

@Data
public class SysFileQueryRequest extends PageRequest {
    private String fileName;

    private Long menuId;

    private String fileType;

    private Integer businessType;
}
