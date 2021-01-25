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

    @PostMapping("/upload")
    public JSONObject uploadFile(@RequestBody UploadFile uploadFile){
        log.info("upload file. project: {}, file: {} ", uploadFile.getProjectName(), uploadFile.getFileName());
        JSONObject ret = new JSONObject();

        // TODO change upload result, and update fileInfo
        UpdateWrapper<FileInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("projectName", uploadFile.getProjectName())
                    .eq("fileName", uploadFile.getFileName())
                    .set("uploader", uploadFile.getUploader())
                    .set("uploadTime", uploadFile.getUploadTime())
                    .set("isUploaded", true);

        int result = fileInfoMapper.update(null, updateWrapper);
        log.info("result: {}", result);
        if(result == 1){
            ret.put("code", "upload ok");
        }else{
            ret.put("code", "upload error");
        }
        return ret;
    }
}
