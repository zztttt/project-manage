package org.fields.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("fileInfo")
public class FileInfo {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField("projectName")
    private String projectName;
    @TableField("fileName")
    private String fileName;
    @TableField("uploadResult")
    private String uploadResult;
    @TableField("uploader")
    private String uploader;
    @TableField("uploadTime")
    private String uploadTime;
    @TableField("isUploaded")
    private Boolean isUploaded;
}
