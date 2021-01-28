package org.fields.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.fields.project.common.RespResult;
import org.fields.project.config.Constant;
import org.fields.project.entity.request.CreateProject;
import org.fields.project.entity.request.CreateTable;
import org.fields.project.entity.request.DeleteProject;
import org.fields.project.entity.request.UpdateProjectStatus;
import org.fields.project.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
public class ProjectManageController {
    @Autowired
    Utils utils;

    @PostMapping("/init")
    public RespResult initTable(@RequestBody CreateTable createTable){
        log.info("create table: {}", createTable.getTableName());
        String tableName = createTable.getTableName();
        List<String> columns = createTable.getColumns();
        if(!utils.isTableExisting(tableName)){
            List<String> types = new ArrayList<>();
            for(String column: columns){
                types.add("varchar(30)");
            }
            Boolean status = utils.createTable(tableName, columns, types);
            return status? RespResult.success(""):RespResult.fail();
        }else{
            return RespResult.fail(400L, tableName + " 已存在");
        }
    }

    @PostMapping("/create")
    public RespResult createProject(@RequestBody CreateProject createProject){
        log.info("create project: {}", createProject);
        String tableName = createProject.getTableName();
        String contextStr = createProject.getContext();
        JSONObject context = JSONObject.parseObject(contextStr);
        String projectName = (String) context.get("xmmc");

        if(!utils.isTableExisting(tableName)){
            return RespResult.fail(400L, tableName + " 不存在");
        }
        if(utils.queryOneLine(tableName, "projectName", projectName)){
            log.info("project {} is already existing.", projectName);
            throw new ApiException("project is already existing");
        }

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = simpleDateFormat.format(date);
        List<String> values = new ArrayList<String>(){{
            add((String) context.get("xmmc"));
            add((String) context.get("tzf"));
            add((String) context.get("xmlx"));
            add((String) context.get("xmlb"));
            add(dateStr);
            add(Constant.STATUS_DEFAULT);
        }};

        Boolean status = utils.insertOneLine(tableName, values);
        log.info("create result: {}", status);
        return status? RespResult.success(""):RespResult.fail();
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
    public RespResult updateProjectStatus(@RequestBody UpdateProjectStatus updateProjectStatus){
        log.info("updateProjectStatus: {}", updateProjectStatus);
        String tableName = updateProjectStatus.getTableName();
        String contextStr = updateProjectStatus.getContext();
        JSONObject context = JSONObject.parseObject(contextStr);
        String projectName = (String) context.get("xmmc");
        if(!utils.queryOneLine(tableName, "projectName", projectName)){
            log.info("table:{} projectName:{} is not existing.", tableName, projectName);
            throw new ApiException("project is not existing");
        }

        Set<String> keys = context.keySet();
        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for(String key: keys){
            if(!key.equals("xmmc")){
                columns.add(front2End(key));
                values.add((String) context.get(key));
            }
        }

        Boolean status = utils.updateOneLine(tableName, "projectName", projectName, columns, values);
        log.info("update result: {}", status);
        return status? RespResult.success(""):RespResult.fail();
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

    private String back2Front(String src){
        String ret = null;
        switch (src){
            case "projectName":
                ret = "xmmc";
                break;
            case "projectType":
                ret = "xmlx";
                break;
            case "projectCategory":
                ret = "xmlb";
                break;
            case "investor":
                ret = "tzf";
                break;
            default:
                assert false;
        }
        return ret;
    }

    private String front2End(String src){
        String ret = null;
        switch (src){
            case "xmmc":
                ret = "projectName";
                break;
            case "xmlx":
                ret = "projectType";
                break;
            case "xmlb":
                ret = "projectCategory";
                break;
            case "tzf":
                ret = "investor";
                break;
            default:
                assert false;
        }
        return ret;
    }
}
