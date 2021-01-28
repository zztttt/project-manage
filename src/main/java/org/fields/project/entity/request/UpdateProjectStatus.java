package org.fields.project.entity.request;

import lombok.Data;

@Data
public class UpdateProjectStatus {
    private String tableName;
    private String context;
}
