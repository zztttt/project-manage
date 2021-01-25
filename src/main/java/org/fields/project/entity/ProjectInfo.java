package org.fields.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("projectInfo")
public class ProjectInfo {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @TableField("projectName")
    private String projectName;
    @TableField("investor")
    private String investor;
    @TableField("projectType")
    private String projectType;
    @TableField("projectCategory")
    private String projectCategory;
    @TableField("date")
    private String date;
    @TableField("status")
    private String status;
}
