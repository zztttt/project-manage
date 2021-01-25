package org.fields.project.entity.request;

import lombok.Data;

@Data
public class UpdateProjectStatus {
    private String projectName;
    private String newStatus;
}
