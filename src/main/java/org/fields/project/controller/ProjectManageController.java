package org.fields.project.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.fields.project.common.RespResult;
import org.fields.project.config.Constant;
import org.fields.project.entity.request.*;
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

    // 用不着
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

    // 新增项目
    @PostMapping("/create")
    public RespResult createProject(@RequestBody CreateProject createProject){
        log.info("create project: {}", createProject);
        String tableName = createProject.getTableName();
        String contextStr = createProject.getContext();
        JSONObject context = JSONObject.parseObject(contextStr);
        String projectName = (String) context.get("xmmc");

        // 如果存项目的表不存在，则初始化这张表
        if(!utils.isTableExisting(tableName)){
            // init table
            Set<String> keys = context.keySet();
            List<String> columns = new ArrayList<>(keys);
            List<String> columnTypes = new ArrayList<>();
            for(String key: keys){
                columnTypes.add("varchar(30)");
            }
            //date and status
            columns.add("date");columns.add("status");
            columnTypes.add("varchar(30)");columnTypes.add("varchar(30)");
            Boolean status = utils.createTable(tableName, columns, columnTypes);
            //return status?RespResult.success(""):RespResult.fail(400L, tableName + " 创建失败");
        }
        if(utils.queryOneLine(tableName, "xmmc", projectName)){
            log.info("project {} is already existing.", projectName);
            return RespResult.fail(400L, projectName + " 已存在");
        }

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = simpleDateFormat.format(date);


        List<String> values = new ArrayList<String>(){{
            add((String) context.get("tzf"));
            add((String) context.get("xmmc"));
            add((String) context.get("xmlb"));
            add((String) context.get("xmlx"));
            add(dateStr);
            add(Constant.STATUS_DEFAULT);
        }};

        Boolean status = utils.insertOneLine(tableName, values);
        log.info("create result: {}", status);
        return status? RespResult.success(""):RespResult.fail();
    }


    // 更新项目状态
    @PostMapping("/update")
    public RespResult updateProjectStatus(@RequestBody UpdateProjectStatus updateProjectStatus){
        log.info("updateProjectStatus: {}", updateProjectStatus);
        String tableName = updateProjectStatus.getTableName();
        String contextStr = updateProjectStatus.getContext();
        JSONObject context = JSONObject.parseObject(contextStr);
        //String projectName = (String) context.get("xmmc");
        //Integer id = (Integer) context.get("id");
        String id = (String) context.get("id");
        if(!utils.queryOneLine(tableName, "id", id)){
            log.info("table:{} id:{} is not existing.", tableName, id);
            //throw new ApiException("project is not existing");
            return RespResult.fail(500L, "项目不存在");
        }

        Set<String> keys = context.keySet();
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for(String key: keys){
            if(!key.equals("id")){
                columns.add(key);
                values.add(context.get(key));
            }
        }

        Boolean status = utils.updateOneLine(tableName, "id", id, columns, values);
        log.info("update result: {}", status);
        return status? RespResult.success(""):RespResult.fail();
    }

    //上传项目文件
    @PostMapping("/uploadFile")
    public RespResult updateFileStatus(@RequestBody UploadFile uploadFile){
        log.info("uploadFile: {}", uploadFile);
        String tableName = uploadFile.getTableName();
        String contextStr = uploadFile.getContext();
        JSONObject context = JSONObject.parseObject(contextStr);

        String xmmc = (String) context.get("xmmc");
//        String fileName = (String) context.get("fileName");
//        String result = (String) context.get("result");
//        String uploader = (String) context.get("uploader");
//        String uploadTime = (String) context.get("uploadTime");

        Boolean status = true;
        Set<String> keys = context.keySet();
        if(!utils.isTableExisting(tableName)){
            // init table
            List<String> columns = new ArrayList<>();
            List<String> columnTypes = new ArrayList<>();
            for(String key: keys){
                columns.add(key);
                columnTypes.add("varchar(30)");
            }
            status = status && utils.createTable(tableName, columns, columnTypes);
        }
        status = status && utils.insertOneColumnOfOneLine(tableName, xmmc, "xmmc", (String) context.get("xmmc"));
        for(String key: keys){
            if(!key.equals("xmmc"))
                status = status && utils.insertOneColumnOfOneLine(tableName, xmmc, key, (String) context.get(key));
        }
        log.info("uploadFile result: {}", status);
        return status?RespResult.success(""):RespResult.fail();
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
