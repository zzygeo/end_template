package com.zzy.biaohui.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.biaohui.common.Constants;
import com.zzy.biaohui.common.ErrorCode;
import com.zzy.biaohui.exception.BusinessException;
import com.zzy.biaohui.manager.AsyncManager;
import com.zzy.biaohui.mapper.SysFileMapper;
import com.zzy.biaohui.model.dto.sysfile.SysFileQueryRequest;
import com.zzy.biaohui.model.dto.sysfile.SysFileUpdateRequest;
import com.zzy.biaohui.model.dto.sysfile.SysFileUploadRequest;
import com.zzy.biaohui.model.dto.sysfile.ThumbnailUploadRequest;
import com.zzy.biaohui.model.entity.SysFile;
import com.zzy.biaohui.model.vo.MenuFileNums;
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
import java.util.Map;
import java.util.TimerTask;
import java.util.stream.Collectors;

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
    public SysFile addSysFile(SysFileUploadRequest sysFileUploadRequest) {
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
            String relativePath = fileDate + File.separator + String.format("%s.%s", md5Name, suffix);
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
            return sysFile;
        } catch (Exception e) {
            log.error("文件上传失败, {}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件上传失败");
        }
    }


    @Override
    public SysFile addSysFile2(SysFileUploadRequest sysFileUploadRequest, String fileName) {
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
            String mainName = fileName;
            // 获取相对路径
            String fileDate = DateUtils.getFileDate();
            String relativePath = fileDate + File.separator + String.format("%s.%s", md5Name, suffix);
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
            return sysFile;
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
            queryWrapper.orderByAsc(Constants.SORT_ORDER_ASC.equals(sortOrder), SysFile.getLambda(sortField));
        }
        return this.list(queryWrapper);
    }

    @Override
    public Page<SysFile> pageSysFile(SysFileQueryRequest sysFileQueryRequest) {
        ThrowUtils.throwIf(sysFileQueryRequest == null, ErrorCode.PARAMS_ERROR);
        String fileName = sysFileQueryRequest.getFileName();
        Long menuId = sysFileQueryRequest.getMenuId();
        String fileType = sysFileQueryRequest.getFileType();
        Integer businessType = sysFileQueryRequest.getBusinessType();
        String sortField = sysFileQueryRequest.getSortField();
        String sortOrder = sysFileQueryRequest.getSortOrder();
        long current = sysFileQueryRequest.getCurrent();
        long pageSize = sysFileQueryRequest.getPageSize();

        LambdaQueryWrapper<SysFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(fileName), SysFile::getFileName, fileName);
        queryWrapper.eq(menuId != null, SysFile::getMenuId, menuId);
        queryWrapper.eq(StringUtils.isNotBlank(fileType), SysFile::getFileType, fileType);
        queryWrapper.eq(businessType != null, SysFile::getBusinessType, businessType);
        if (StringUtils.isNotBlank(sortField)) {
            queryWrapper.orderByDesc(Constants.SORT_ORDER_DESC.equals(sortOrder), SysFile.getLambda(sortField));
            queryWrapper.orderByAsc(Constants.SORT_ORDER_ASC.equals(sortOrder), SysFile.getLambda(sortField));
        }
        Page<SysFile> sysFilePage = new Page<>(current, pageSize);
        return this.page(sysFilePage, queryWrapper);
    }

    @Override
    public boolean containsFile(Long menuId) {
        ThrowUtils.throwIf(menuId == null, ErrorCode.PARAMS_ERROR);
        LambdaQueryWrapper<SysFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysFile::getMenuId, menuId);
        return this.count(queryWrapper) > 0;
    }

    @Override
    public List<MenuFileNums> listMenuFileNums() {
        QueryWrapper<SysFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("menu_id", "count(*) as nums");
        queryWrapper.groupBy("menu_id");
        List<Map<String, Object>> maps = this.baseMapper.selectMaps(queryWrapper);
        List<MenuFileNums> collect = maps.stream().map(map -> {
            MenuFileNums menuFileNums = new MenuFileNums();
            menuFileNums.setMenuId((Long) map.get("menu_id"));
            menuFileNums.setFileNums((Long) map.get("nums"));
            return menuFileNums;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public boolean uploadThumbnail(ThumbnailUploadRequest thumbnailUploadRequest) {
        ThrowUtils.throwIf(thumbnailUploadRequest == null, ErrorCode.PARAMS_ERROR);
        // 文件是否存在
        Long id = thumbnailUploadRequest.getId();
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR);
        SysFile sysFile = this.getById(id);
        ThrowUtils.throwIf(sysFile == null, ErrorCode.PARAMS_ERROR, "主文件不存在");
        MultipartFile file = thumbnailUploadRequest.getFile();
        ThrowUtils.throwIf(file == null || file.getSize() <= 0, ErrorCode.PARAMS_ERROR, "缩略图不能为空");
        try {
            String name = file.getOriginalFilename();
            // 文件计算md5名称
            String md5Name = DigestUtil.md5Hex(file.getInputStream());
            // 获取后缀
            String suffix = FileUtil.extName(name);
            // 获取相对路径
            String fileDate = DateUtils.getFileDate();
            String relativePath = fileDate + File.separator + String.format("%s.%s", md5Name, suffix);
            // 获取绝对路径
            String absolutePath = uploadPath + File.separator + relativePath;
            // 获取大小
            File dest = new File(absolutePath);
            // 判断文件父目录是否存在
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            // 保存文件
            file.transferTo(dest);
            String oldThumbnail = sysFile.getThumbnail();
            sysFile.setThumbnail(relativePath);
            boolean isSuccess = this.updateById(sysFile);
            if (!isSuccess) {
                // 失败删除新上传的缩略图
                try {
                    boolean del = FileUtil.del(dest);
                    if (!del) {
                        log.error("删除文件失败, {}", dest.getAbsoluteFile());
                    }
                } catch (Exception e) {
                    log.error("删除文件失败, {}, 文件路径：{}", e.getMessage(), dest.getAbsoluteFile());
                }
            } else {
                // 成功的话删除旧缩略图
                if (StringUtils.isNotBlank(oldThumbnail)) {
                    String oldThumbnailPath = uploadPath + File.separator + oldThumbnail;
                    try {
                        boolean del = FileUtil.del(oldThumbnailPath);
                        if (!del) {
                            log.error("删除旧缩略图失败, {}", oldThumbnailPath);
                        }
                    } catch (Exception e) {
                        log.error("删除旧缩略图失败, {}, 文件路径：{}", e.getMessage(), oldThumbnailPath);
                    }
                }
            }
            return isSuccess;
        } catch (Exception e) {
            log.error("文件上传失败, {}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件上传失败");
        }
    }

    /**
     * 批量删除文件
     *
     * @param ids
     * @return
     */
    @Override
    public boolean batchDelete(List<Long> ids) {
        ThrowUtils.throwIf(ids == null || ids.size() <= 0, ErrorCode.PARAMS_ERROR);
        List<SysFile> sysFiles = this.listByIds(ids);
        ThrowUtils.throwIf(sysFiles.size() <= 0, ErrorCode.PARAMS_ERROR, "要删除的文件不存在");
        boolean b = this.removeByIds(ids);
        if (b) {
            // 删除文件
            for (SysFile sysFile : sysFiles) {
                String absolutePath = uploadPath + File.separator + sysFile.getFileUrl();
                AsyncManager.me().execute(new TimerTask() {
                    @Override
                    public void run() {
                        int tryTimes = 0;
                        while (tryTimes < 3) {
                            tryTimes++;
                            try {
                                boolean del = FileUtil.del(absolutePath);
                                if (!del) {
                                    log.error("删除文件失败, {}", absolutePath);
                                } else {
                                    log.info("删除文件成功, {}", absolutePath);
                                    break;
                                }
                            } catch (Exception e) {
                                log.error("删除文件失败, {}, 文件路径：{}", e.getMessage(), absolutePath);
                            }
                            try {
                                Thread.sleep(1000 * (long) Math.pow(2, tryTimes - 1));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }, 100);
            }
        } else {
            log.error("删除文件失败, {}", ids);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除文件失败");
        }
        return true;
    }
}




