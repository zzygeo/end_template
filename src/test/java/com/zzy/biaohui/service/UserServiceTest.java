package com.zzy.biaohui.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.zzy.biaohui.model.dto.sysfile.SysFileUploadRequest;
import com.zzy.biaohui.model.dto.sysfile.ThumbnailUploadRequest;
import com.zzy.biaohui.model.entity.SysFile;
import com.zzy.biaohui.model.entity.UcModelAttachment;
import com.zzy.biaohui.utils.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务测试
 *
 * @author zzy
 */
@SpringBootTest
class UserServiceTest {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Resource
    private UserService userService;

    @Autowired
    private UcModelAttachmentService ucModelAttachmentService;

    @Autowired
    private SysFileService sysFileService;

    // 待测试

//    @Test
    public void test() {
        long l = userService.userRegister("admin", "12345678", "12345678");
        System.out.println(l);
    }

    @Test
    public void testInsertModel() {
        List<UcModelAttachment> list = ucModelAttachmentService.list();
        String relativePath = "D:\\browser_download";
        List<UcModelAttachment> collect = list.stream().filter(e -> e.getUrl().contains(".glb") && !e.getUrl().contains("http")).collect(Collectors.toList());
        for (UcModelAttachment e : collect) {
            Path gldPath = Path.of(relativePath + File.separator +e.getUrl());
            File file = gldPath.toFile();
            if (file.exists()) {
                try {
                    MultipartFile multipartFile = convertFileToMultipartFile(file);
                    SysFileUploadRequest sysFileUploadRequest = new SysFileUploadRequest();
                    sysFileUploadRequest.setFile(multipartFile);
                    sysFileUploadRequest.setMenuId(163L);
                    sysFileUploadRequest.setBusinessType(3);
                    SysFile sysFile = sysFileService.addSysFile2(sysFileUploadRequest, e.getFilename());

                    if (StrUtil.isNotBlank(e.getCover()) && e.getCover().contains("uploads")) {
                        Path coverPath = Path.of(relativePath + File.separator + e.getCover());
                        File coverFile = coverPath.toFile();
                        MultipartFile multipartFile2 = convertFileToMultipartFile(coverFile);
                        ThumbnailUploadRequest thumbnailUploadRequest = new ThumbnailUploadRequest();
                        thumbnailUploadRequest.setId(sysFile.getId());
                        thumbnailUploadRequest.setFile(multipartFile2);
                        boolean b = sysFileService.uploadThumbnail(thumbnailUploadRequest);

                        if (!b) {
                            System.out.println("插入封面失败失败");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private SysFile storeFile(File file) {
        String name = file.getName();
        // 文件计算md5名称
        String md5Name = DigestUtil.md5Hex(file);
        // 获取后缀
        String suffix = FileUtil.extName(name);
        String mainName = FileUtil.getPrefix(name);
        // 获取相对路径
        String fileDate = DateUtils.getFileDate();
        String relativePath = fileDate + File.separator + String.format("%s.%s", md5Name, suffix);
        // 获取绝对路径
        String absolutePath = uploadPath + File.separator + relativePath;
        // 获取大小
        long size = file.length();
        File dest = new File(absolutePath);
        // 判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        // 保存文件
        FileUtil.copy(file, dest,true);
        SysFile sysFile = new SysFile();
        sysFile.setFileName(mainName).setFileUrl(relativePath).setMd5Name(md5Name)
                .setMenuId(163L).setFileSize(size).setFileType(suffix).setBusinessType(3);
        return sysFile;
    }

    public MultipartFile convertFileToMultipartFile(File file) throws IOException {
        String fileName = file.getName();
        FileInputStream inputStream = new FileInputStream(file);

        // 参数说明：
        // name（表单字段名）, originalFilename（文件名）, contentType（可选）, content（字节流）
        return new MockMultipartFile(
                "file", // 表单字段名（与接口参数名一致）
                fileName,
                null,   // 可以传入 "image/jpeg" 等 MIME 类型，或 null 自动推断
                inputStream
        );
    }
}