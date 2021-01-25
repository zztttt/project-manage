package org.fields.project.entity.request;

import lombok.Data;

@Data
public class CreateProject {
    private String projectName;
    private String investor;
    private String projectType;
    private String projectCategory;
    private String date;
}
