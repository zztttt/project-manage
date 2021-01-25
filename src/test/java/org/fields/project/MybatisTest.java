package org.fields.project;

import lombok.extern.slf4j.Slf4j;
import org.fields.project.entity.ProjectInfo;
import org.fields.project.mapper.ProjectInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisTest {
    @Autowired
    ProjectInfoMapper projectInfoMapper;

    @Test
    public void insertTest(){
        ProjectInfo projectInfo = new ProjectInfo();
        projectInfo.setProjectName("name");
        projectInfo.setInvestor("investor");
        projectInfo.setProjectCategory("category");
        projectInfo.setProjectType("type");
        projectInfo.setDate("20200101");
        projectInfo.setStatus("status");
        int res = projectInfoMapper.insert(projectInfo);
        log.info("insert result: {}", res);
    }
}
