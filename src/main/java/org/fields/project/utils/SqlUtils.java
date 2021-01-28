package org.fields.project.utils;


import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public class SqlUtils {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Boolean isTableExisting(String tableName){
        String sql = "show tables";
        List<String> tables = jdbcTemplate.queryForList(sql, String.class);
        for(String table: tables){
            if(table.toLowerCase(Locale.ROOT).equals(tableName.toLowerCase(Locale.ROOT)))
                return true;
        }
        return false;
    }
    /**
     * create table
     * @return sql
     */
    public String createTable(String tableName, List<String> columns, List<String> columnTypes){
        if(isTableExisting(tableName)){
            log.info("table: {} is already existing", tableName);
            throw new ApiException("table is already existing");
        }
        if(columns.size()!= columnTypes.size()){
            log.info("createTable {}: columns and types size don't match, ", tableName);
            throw new ApiException("createTable columns and types size don't match");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("create table ").append(tableName).append("( id bigint(64) primary key not null auto_increment,");
        int len = columns.size();
        String link = "";
        for(int i = 0; i < len; ++i){
            sb.append(link).append(columns.get(i)).append(" ").append(columnTypes.get(i)).append(" NULL");
            link = ",";
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * insert into t1 (c1, c2...) value (v1, v2...)
     * @param tableName
     * @param columns
     * @param values
     * @return sql
     */
    public String insertOneLine(String tableName, List<String> columns, List<String> values){
        if(columns.size() != values.size()){
            log.info("insertNewLine columns and types size don't match");
            throw new ApiException("insertNewLine columns and types size don't match");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(tableName).append("(");
        String link = "";
        int len = columns.size();
        for(int i = 0;i < len; ++i){
            sb.append(link).append(columns.get(i));
            link = ",";
        }
        sb.append(") value (");
        link = "";
        for(int i = 0; i < len; ++i){
            sb.append(link).append("'").append(values.get(i)).append("'");
            link = ",";
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * delete from t1 where src1='v1'
     * @param tableName
     * @param srcColumn
     * @param srcValue
     * @return
     */
    public String deleteOneLine(String tableName, String srcColumn, String srcValue){
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ").append(tableName).append(" where ")
                .append(srcColumn).append("='").append(srcValue).append("'");
        return sb.toString();
    }

    /**
     * select * from t1 where src1 = 'v1'
     * @param tableName
     * @param srcColumn
     * @param srcValue
     * @return
     */
    public String queryOneLine(String tableName, String srcColumn, String srcValue){
        StringBuilder sb = new StringBuilder();
        sb.append("select * from ").append(tableName).append(" where ").append(srcColumn).append("='").append(srcValue).append("'");
        return sb.toString();
    }

    /**
     * update t1 set dst1='d1' where src1='v1'
     * @param tableName
     * @param srcColumn
     * @param srcValue
     * @param dstColumn
     * @param dstValue
     * @return
     */
    public String updateOneColumnOfOneLine(String tableName, String srcColumn, String srcValue, String dstColumn, String dstValue){
        StringBuilder sb = new StringBuilder();
        sb.append("update ").append(tableName).append(" set ").append(dstColumn).append("='").append(dstValue)
                .append("' where ").append(srcColumn).append("='").append(srcValue).append("'");
        return sb.toString();
    }

    public String queryTableMetadataDetails(String tableName){
        StringBuilder sb = new StringBuilder();
        sb.append("select columnName from ").append(tableName);
        return sb.toString();
    }

//    public String updateTableBatch(String tableName, String srcColumn, String srcValue, List<String> dstColumns, String dstValues){
//
//    }
}

