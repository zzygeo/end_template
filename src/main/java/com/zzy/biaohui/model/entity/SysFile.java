package com.zzy.biaohui.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 
 * @TableName sys_file
 */
@TableName(value ="sys_file")
@Data
@Accessors(chain = true)
public class SysFile implements Serializable {

    private static final Map<String, SFunction<SysFile, ?>> FIELDS = Map.ofEntries(
            Map.entry("id", SysFile::getId),
            Map.entry("fileName", SysFile::getFileName),
            Map.entry("fileUrl", SysFile::getFileUrl),
            Map.entry("md5Name", SysFile::getMd5Name),
            Map.entry("menuId", SysFile::getMenuId),
            Map.entry("fileSize", SysFile::getFileSize),
            Map.entry("fileType", SysFile::getFileType),
            Map.entry("businessType", SysFile::getBusinessType),
            Map.entry("createTime", SysFile::getCreateTime),
            Map.entry("updateTime", SysFile::getUpdateTime)
    );

    public static SFunction<SysFile, ?> getLambda(String fieldName) {
        return FIELDS.get(fieldName);
    }

    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 文件md5
     */
    private String md5Name;

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件类型（后缀）
     */
    private String fileType;

    /**
     * 业务类型 eg: 1-点 2-线 3-面
     */
    private Integer businessType;

    /**
     * 
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date createTime;

    /**
     * 
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysFile other = (SysFile) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
            && (this.getFileUrl() == null ? other.getFileUrl() == null : this.getFileUrl().equals(other.getFileUrl()))
            && (this.getMd5Name() == null ? other.getMd5Name() == null : this.getMd5Name().equals(other.getMd5Name()))
            && (this.getMenuId() == null ? other.getMenuId() == null : this.getMenuId().equals(other.getMenuId()))
            && (this.getFileSize() == null ? other.getFileSize() == null : this.getFileSize().equals(other.getFileSize()))
            && (this.getFileType() == null ? other.getFileType() == null : this.getFileType().equals(other.getFileType()))
            && (this.getBusinessType() == null ? other.getBusinessType() == null : this.getBusinessType().equals(other.getBusinessType()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getFileUrl() == null) ? 0 : getFileUrl().hashCode());
        result = prime * result + ((getMd5Name() == null) ? 0 : getMd5Name().hashCode());
        result = prime * result + ((getMenuId() == null) ? 0 : getMenuId().hashCode());
        result = prime * result + ((getFileSize() == null) ? 0 : getFileSize().hashCode());
        result = prime * result + ((getFileType() == null) ? 0 : getFileType().hashCode());
        result = prime * result + ((getBusinessType() == null) ? 0 : getBusinessType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fileName=").append(fileName);
        sb.append(", fileUrl=").append(fileUrl);
        sb.append(", md5Name=").append(md5Name);
        sb.append(", menuId=").append(menuId);
        sb.append(", fileSize=").append(fileSize);
        sb.append(", fileType=").append(fileType);
        sb.append(", businessType=").append(businessType);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}