package org.fields.project.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.fields.project.config.Constant;
import org.fields.project.entity.ProjectInfo;
import org.fields.project.entity.request.CreateProject;
import org.fields.project.mapper.ProjectInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProjectManageController {
    @Autowired
    ProjectInfoMapper projectInfoMapper;

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

        int res = projectInfoMapper.insert(projectInfo);
        log.info("insert result: {}", res);
        if(res == 1){
            ret.put("code", "ok");
        }else{
            ret.put("code", "error");
        }
        return ret;
    }

}
