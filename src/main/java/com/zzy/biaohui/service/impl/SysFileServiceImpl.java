package com.zzy.biaohui.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.biaohui.common.Constants;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.exception.BusinessException;
import com.zzy.biaohui.mapper.SysFileMapper;
import com.zzy.biaohui.model.dto.sysfile.SysFileQueryRequest;
import com.zzy.biaohui.model.dto.sysfile.SysFileUpdateRequest;
import com.zzy.biaohui.model.dto.sysfile.SysFileUploadRequest;
import com.zzy.biaohui.model.entity.SysFile;
import com.zzy.biaohui.service.SysFileService;
import com.zzy.biaohui.utils.DateUtils;
import com.zzy.biaohui.utils.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @author zzy
 * @description 针对表【sys_file】的数据库操作Service实现
 * @createDate 2025-07-04 15:25:49
 */
@Service
@Slf4j
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile>
        implements SysFileService {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public boolean addSysFile(SysFileUploadRequest sysFileUploadRequest) {
        MultipartFile file = sysFileUploadRequest.getFile();
        Long menuId = sysFileUploadRequest.getMenuId();
        Integer businessType = sysFileUploadRequest.getBusinessType();

        ThrowUtils.throwIf(file == null, ErrorCode.PARAMS_ERROR, "文件不能为空");
        ThrowUtils.throwIf(menuId == null, ErrorCode.PARAMS_ERROR, "菜单id不能为空");
        if (businessType == null) {
            businessType = 0;
        }
        try {
            String name = file.getOriginalFilename();
            // 文件计算md5名称
            String md5Name = DigestUtil.md5Hex(file.getInputStream());
            // 获取后缀
            String suffix = FileUtil.extName(name);
            String mainName = FileUtil.getPrefix(name);
            // 获取相对路径
            String fileDate = DateUtils.getFileDate();
            String relativePath = fileDate + File.separator + String.format("%s.%s",md5Name, suffix);
            // 获取绝对路径
            String absolutePath = uploadPath + File.separator + relativePath;
            // 获取大小
            long size = file.getSize();
            File dest = new File(absolutePath);
            // 判断文件父目录是否存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            // 保存文件
            file.transferTo(dest);
            SysFile sysFile = new SysFile();
            sysFile.setFileName(mainName).setFileUrl(relativePath).setMd5Name(md5Name)
                    .setMenuId(menuId).setFileSize(size).setFileType(suffix).setBusinessType(businessType);
            boolean isSuccess = this.save(sysFile);
            if (!isSuccess) {
                // 删除文件
                try {
                    boolean del = FileUtil.del(dest);
                    if (!del) {
                        log.error("删除文件失败, {}", dest.getAbsoluteFile());
                    }
                } catch (Exception e) {
                    log.error("删除文件失败, {}, 文件路径：{}", e.getMessage(), dest.getAbsoluteFile());
                }
            }
            return true;
        } catch (Exception e) {
            log.error("文件上传失败, {}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件上传失败");
        }
    }

    @Override
    public boolean deleteSysFile(Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        SysFile sysFileDB = this.getById(id);
        ThrowUtils.throwIf(sysFileDB == null, ErrorCode.PARAMS_ERROR, "要删除的文件不存在");
        boolean b = this.removeById(id);
        // 如果成功删除，则删除文件
        if (b) {
            // 删除文件
            String absolutePath = uploadPath + File.separator + sysFileDB.getFileUrl();
            try {
                boolean del = FileUtil.del(absolutePath);
                if (!del) {
                    log.error("删除文件失败, {}", absolutePath);
                } else {
                    log.info("删除文件成功, {}", absolutePath);
                }
            } catch (Exception e) {
                log.error("删除文件失败, {}, 文件路径：{}", e.getMessage(), absolutePath);
            }
        }
        return b;
    }

    @Override
    public boolean updateSysFile(SysFileUpdateRequest sysFileUpdateRequest) {
        ThrowUtils.throwIf(sysFileUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        Long id = sysFileUpdateRequest.getId();
        String fileName = sysFileUpdateRequest.getFileName();
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR);
        if (fileName != null && StringUtils.isBlank(fileName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件名不能为空白");
        }
        SysFile sysFile = new SysFile();
        BeanUtils.copyProperties(sysFileUpdateRequest, sysFile);
        return this.updateById(sysFile);
    }

    @Override
    public List<SysFile> listSysFile(SysFileQueryRequest sysFileQueryRequest) {
        ThrowUtils.throwIf(sysFileQueryRequest == null, ErrorCode.PARAMS_ERROR);
        String fileName = sysFileQueryRequest.getFileName();
        Long menuId = sysFileQueryRequest.getMenuId();
        String fileType = sysFileQueryRequest.getFileType();
        Integer businessType = sysFileQueryRequest.getBusinessType();
        String sortField = sysFileQueryRequest.getSortField();
        String sortOrder = sysFileQueryRequest.getSortOrder();

        LambdaQueryWrapper<SysFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(fileName), SysFile::getFileName, fileName);
        queryWrapper.eq(menuId != null, SysFile::getMenuId, menuId);
        queryWrapper.eq(StringUtils.isNotBlank(fileType), SysFile::getFileType, fileType);
        queryWrapper.eq(businessType != null, SysFile::getBusinessType, businessType);
        if (StringUtils.isNotBlank(sortField)) {
            queryWrapper.orderByDesc(Constants.SORT_ORDER_DESC.equals(sortOrder), SysFile.getLambda(sortField));
        }


        return this.list(queryWrapper);
    }

    @Override
    public boolean containsFile(Long menuId) {
        ThrowUtils.throwIf(menuId == null, ErrorCode.PARAMS_ERROR);
        LambdaQueryWrapper<SysFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysFile::getMenuId, menuId);
        return this.count(queryWrapper) > 0;
    }
}




