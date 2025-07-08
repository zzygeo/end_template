package com.zzy.biaohui.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.biaohui.model.dto.sysfile.SysFileQueryRequest;
import com.zzy.biaohui.model.dto.sysfile.SysFileUpdateRequest;
import com.zzy.biaohui.model.dto.sysfile.SysFileUploadRequest;
import com.zzy.biaohui.model.entity.SysFile;

import java.util.List;

/**
* @author zzy
* @description 针对表【sys_file】的数据库操作Service
* @createDate 2025-07-04 15:25:49
*/
public interface SysFileService extends IService<SysFile> {
    boolean addSysFile(SysFileUploadRequest sysFileUploadRequest);

    boolean deleteSysFile(Long id);

    boolean updateSysFile(SysFileUpdateRequest sysFileUpdateRequest);

    List<SysFile> listSysFile(SysFileQueryRequest sysFileQueryRequest);

    boolean containsFile(Long menuId);
}
