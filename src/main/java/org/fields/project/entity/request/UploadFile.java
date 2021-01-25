package org.fields.project.entity.request;

import lombok.Data;

@Data
public class UploadFile {
    private String projectName;
    private String fileName;
    private String uploader;
    private String uploadTime;
}
