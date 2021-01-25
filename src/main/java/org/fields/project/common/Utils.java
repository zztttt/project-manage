package org.fields.project.common;

import org.fields.project.config.Constant;
import org.fields.project.entity.FileInfo;
import org.fields.project.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Utils {
    @Autowired
    FileInfoMapper fileInfoMapper;
}
