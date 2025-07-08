package com.zzy.biaohui.model.dto.sysfile;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SysFileUploadRequest {
    private MultipartFile file;

    private Long menuId;

    private Integer businessType;
}
