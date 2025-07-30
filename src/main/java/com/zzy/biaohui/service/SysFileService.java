package com.zzy.biaohui.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.biaohui.model.dto.sysfile.SysFileQueryRequest;
import com.zzy.biaohui.model.dto.sysfile.SysFileUpdateRequest;
import com.zzy.biaohui.model.dto.sysfile.SysFileUploadRequest;
import com.zzy.biaohui.model.dto.sysfile.ThumbnailUploadRequest;
import com.zzy.biaohui.model.entity.SysFile;
import com.zzy.biaohui.model.vo.MenuFileNums;

import java.util.List;

/**
* @author zzy
* @description 针对表【sys_file】的数据库操作Service
* @createDate 2025-07-04 15:25:49
*/
public interface SysFileService extends IService<SysFile> {
    SysFile addSysFile(SysFileUploadRequest sysFileUploadRequest);


    SysFile addSysFile2(SysFileUploadRequest sysFileUploadRequest, String fileName);

    boolean deleteSysFile(Long id);

    boolean updateSysFile(SysFileUpdateRequest sysFileUpdateRequest);

    List<SysFile> listSysFile(SysFileQueryRequest sysFileQueryRequest);

    Page<SysFile> pageSysFile(SysFileQueryRequest sysFileQueryRequest);

    boolean containsFile(Long menuId);

    List<MenuFileNums> listMenuFileNums();

    boolean uploadThumbnail(ThumbnailUploadRequest thumbnailUploadRequest);

    boolean batchDelete(List<Long> ids);
}
