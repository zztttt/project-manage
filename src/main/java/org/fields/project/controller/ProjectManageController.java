package org.fields.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.fields.project.common.Utils;
import org.fields.project.config.Constant;
import org.fields.project.entity.FileInfo;
import org.fields.project.entity.ProjectInfo;
import org.fields.project.entity.request.CreateProject;
import org.fields.project.entity.request.DeleteProject;
import org.fields.project.entity.request.UpdateProjectStatus;
import org.fields.project.mapper.FileInfoMapper;
import org.fields.project.mapper.ProjectInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class ProjectManageController {
    @Autowired
    ProjectInfoMapper projectInfoMapper;
    @Autowired
    FileInfoMapper fileInfoMapper;

    @PostMapping("/create")
    public JSONObject createProject(@RequestBody CreateProject createProject){
        log.info("create project: {}", createProject);
        JSONObject ret = new JSONObject();

        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setProjectName(createProject.getProjectName());
        projectInfo.setInvestor(createProject.getInvestor());
        projectInfo.setProjectType(createProject.getProjectType());
        projectInfo.setProjectCategory(createProject.getProjectCategory());
        projectInfo.setDate(createProject.getDate());
        projectInfo.setStatus(Constant.STATUS_DEFAULT);

        int result = projectInfoMapper.insert(projectInfo);
        generateDefaultFiles(createProject.getProjectName());

        log.info("insert result: {}", result);
        if(result == 1){
            ret.put("code", "create ok");
        }else{
            ret.put("code", "create error");
        }
        return ret;
    }

    @PostMapping("delete")
    public JSONObject deleteProject(@RequestBody DeleteProject deleteProject){
        log.info("delete project: {}", deleteProject);
        JSONObject ret = new JSONObject();

        UpdateWrapper<ProjectInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("projectName", deleteProject.getProjectName());
        int result = projectInfoMapper.delete(updateWrapper);
        log.info("update result: {}", result);
        if(result == 1){
            ret.put("code", "delete ok");
        }else{
            ret.put("code", "delete error");
        }
        return ret;
    }

    @PostMapping("/update")
    public JSONObject updateProjectStatus(@RequestBody UpdateProjectStatus updateProjectStatus){
        log.info("updateProjectStatus: {}", updateProjectStatus);
        JSONObject ret = new JSONObject();

        UpdateWrapper<ProjectInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("projectName", updateProjectStatus.getProjectName()).set("status", updateProjectStatus.getNewStatus());
        int result = projectInfoMapper.update(null, updateWrapper);
        log.info("update result: {}", result);
        if(result == 1){
            ret.put("code", "update ok");
        }else{
            ret.put("code", "update error");
        }
        return ret;
    }

    public void generateDefaultFiles(String projectName){
        List<String> defaultFiles = new ArrayList<String>(){{
            add("项目核准文件");
            add("项目获得指标的政府批复文件");
            add("项目备案文件");
            add("规划选址意见书");
            add("国土土地预审意见");
            add("环保影响评估报告及审批意见");
            add("环保影响评估报告");
            add("环保影响评估报告审批意见");
            add("安全评价报告及审批意见");
            add("安全评价报告");
        }};
        for(String fileName: defaultFiles){
            FileInfo fileInfo = new FileInfo();
            fileInfo.setProjectName(projectName);
            fileInfo.setFileName(fileName);
            fileInfo.setUploadResult(Constant.UPLOAD_RESULT_DEFAULT);
            fileInfo.setUploader(Constant.UPLOADER_DEFAULT);
            fileInfo.setUploadTime(Constant.UPLOAD_TIME_DEFAULT);
            fileInfo.setIsUploaded(false);
            int result = fileInfoMapper.insert(fileInfo);
            assert result == 1;
        }
    }
}
