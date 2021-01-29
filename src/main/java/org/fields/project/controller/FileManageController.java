package org.fields.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.fields.project.config.Constant;
import org.fields.project.entity.FileInfo;
import org.fields.project.entity.request.UploadFile;
import org.fields.project.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FileManageController {
    @Autowired
    FileInfoMapper fileInfoMapper;

}
