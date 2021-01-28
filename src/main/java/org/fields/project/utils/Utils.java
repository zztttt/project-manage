package org.fields.project.utils;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class Utils {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    SqlUtils sqlUtils;

    public Boolean isTableExisting(String tableName){
        return sqlUtils.isTableExisting(tableName);
    }


    public Boolean createTable(String tableName, List<String> columns, List<String> columnsTypes){
        if(!isTableExisting("tableMetadataDetail")){
            List<String> c = new ArrayList<String>(){{
                add("tableName");add("columnName");add("columnType");
            }};
            List<String> ct = new ArrayList<String>(){{
                add("varchar(20)");add("varchar(20)");add("varchar(20)");
            }};
            String sql = sqlUtils.createTable("tableMetadataDetail", c, ct);
            jdbcTemplate.execute(sql);
        }
        // create table
        String sql = sqlUtils.createTable(tableName, columns, columnsTypes);
        jdbcTemplate.execute(sql);
        // insert tableMetadata
        Boolean status = true;
        int len = columns.size();
        for(int i = 0; i < len; ++i){
            List<String> metadataColumn = new ArrayList<String>(){{
                add("tableName");add("columnName");add("columnType");
            }};
            int index = i;
            List<String> metadataColumnValue = new ArrayList<String>(){{
                add(tableName);add(columns.get(index));add(columnsTypes.get(index));
            }};
            String metaDataSql = sqlUtils.insertOneLine("tableMetadataDetail", metadataColumn, metadataColumnValue);
            int ret = jdbcTemplate.update(metaDataSql);
            status = status &&  ret == 1;
        }
        return status;
    }

    public Boolean insertOneLine(String tableName, List<String> values){
        List<String> columns = queryTableColumns(tableName);
        if(columns.size() != values.size()){
            log.info("values count doesn't match column count");
            throw new ApiException("values count doesn't match column count");
        }

        String sql = sqlUtils.insertOneLine(tableName, columns, values);
        int ret = jdbcTemplate.update(sql);
        return ret == 1;
    }

    public Boolean deleteOneLine(String tableName, String srcColumn, String srcValue){
        if(!queryOneLine(tableName, srcColumn, srcValue)){
            log.info("line is not existing.");
            throw new ApiException("delete line error. line is not existing.");
        }

        String sql = sqlUtils.deleteOneLine(tableName, srcColumn, srcValue);
        int ret = jdbcTemplate.update(sql);
        return ret == 1;
    }

    public Boolean queryOneLine(String tableName, String srcColumn, String srcValue){
        String sql = sqlUtils.queryOneLine(tableName, srcColumn, srcValue);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        if(results.size() > 1){
            log.info("more than 1 line");
            throw new ApiException("queryOneLine error. more than one line.");
        }
        return results.size() == 1;
    }

    /**
     * update t1 set dstColumn=dstValue where srcColumn=srcValue
     * @param tableName
     * @param srcColumn
     * @param srcValue
     * @param dstColumn
     * @param dstValue
     * @return
     */
    public Boolean updateOneLine(String tableName, String srcColumn, String srcValue, String dstColumn, String dstValue){
        if(!queryOneLine(tableName, srcColumn, srcValue)){
            log.info("updateOneLine error. there is no line");
            throw new ApiException("updateOneLine error. there is no line");
        }
        String sql = sqlUtils.updateOneColumnOfOneLine(tableName, srcColumn, srcValue, dstColumn, dstValue);
        int result = jdbcTemplate.update(sql);
        return result == 1;
    }

    public Boolean updateOneLine(String tableName, String srcColumn, String srcValue, List<String> columns, List<String> values){
        if(!queryOneLine(tableName, srcColumn, srcValue)){
            log.info("updateOneLine error. there is no line");
            throw new ApiException("updateOneLine error. there is no line");
        }
        String sql = sqlUtils.updateOneLine(tableName, srcColumn, srcValue, columns, values);
        log.info("update :{}", sql);
        int result = jdbcTemplate.update(sql);
        return result == 1;
    }

    /**
     * 查询一个table的所有列属性有哪些，以供insert操作
     * @param tableName
     * @return
     */
    public List<String> queryTableColumns(String tableName){
        String sql = "select columnName from tableMetadataDetail where tableName = '" + tableName + "'";
        List<String> ret = jdbcTemplate.queryForList(sql, String.class);
        return ret;
    }
}

