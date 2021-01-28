package org.fields.project.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.fields.project.config.Constant;
import org.fields.project.entity.request.CreateProject;
import org.fields.project.entity.request.DeleteProject;
import org.fields.project.entity.request.UpdateProjectStatus;
import org.fields.project.utils.Utils;
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
    Utils utils;

    @PostMapping("/create")
    public JSONObject createProject(@RequestBody CreateProject createProject){
        log.info("create project: {}", createProject);
        JSONObject ret = new JSONObject();

        if(!utils.isTableExisting("projectInfo")){
            List<String> columns = new ArrayList<String>(){{
                add("projectName");add("investor");add("projectType");
                add("projectCategory");add("date");add("status");
            }};
            List<String> columnTypes = new ArrayList<String>(){{
                add("varchar(50)");add("varchar(50)");add("varchar(20)");
                add("varchar(20)");add("varchar(10)");add("varchar(20)");
            }};
            utils.createTable("projectInfo", columns, columnTypes);
        }

        List<String> values = new ArrayList<String>(){{
            add(createProject.getProjectName());
            add(createProject.getInvestor());
            add(createProject.getProjectType());
            add(createProject.getProjectCategory());
            add(createProject.getDate());
            add(Constant.STATUS_DEFAULT);
        }};
        Boolean status = utils.insertOneLine("projectInfo", values);

        //generateDefaultFiles(createProject.getProjectName());

        log.info("insert result: {}", status);
        if(status){
            ret.put("code", "create ok");
        }else{
            ret.put("code", "create error");
        }
        return ret;
    }

    @PostMapping("/delete")
    public JSONObject deleteProject(@RequestBody DeleteProject deleteProject){
        log.info("delete project: {}", deleteProject);
        JSONObject ret = new JSONObject();
        Boolean status = utils.deleteOneLine("projectInfo", "projectName", deleteProject.getProjectName());

        log.info("delete result: {}", status);
        if(status){
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
        Boolean status = utils.updateOneLine("projectInfo", "projectName", updateProjectStatus.getProjectName(),"status", updateProjectStatus.getNewStatus());


        log.info("update result: {}", status);
        if(status){
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
//        for(String fileName: defaultFiles){
//            FileInfo fileInfo = new FileInfo();
//            fileInfo.setProjectName(projectName);
//            fileInfo.setFileName(fileName);
//            fileInfo.setUploadResult(Constant.UPLOAD_RESULT_DEFAULT);
//            fileInfo.setUploader(Constant.UPLOADER_DEFAULT);
//            fileInfo.setUploadTime(Constant.UPLOAD_TIME_DEFAULT);
//            fileInfo.setIsUploaded(false);
//            int result = fileInfoMapper.insert(fileInfo);
//            assert result == 1;
//        }
    }
}
