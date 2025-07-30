package com.zzy.biaohui.model.dto.sysfile;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author zzy
 * @Description 缩略图上传
 */
@Data
public class ThumbnailUploadRequest {
    private Long id;
    private MultipartFile file;
}
