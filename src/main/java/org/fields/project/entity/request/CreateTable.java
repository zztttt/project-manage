package org.fields.project.entity.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateTable {
    private String tableName;
    private List<String> columns;
}
