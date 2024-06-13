package com.tistory.framework.jpa.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * 메타데이터 조회 결과를 캐싱하여 성능을 최적화
 */
@Service
public class TableService {
    @Autowired
    private DataSource dataSource;

    private Set<String> tableNames = new HashSet<>();

    @PostConstruct
    public void init() throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try(ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})){
                while(tables.next()){
                    String tableName = tables.getString("TABLE_NAME");
                    tableNames.add(tableName);
                }
            }
        }
    }

    public Set<String> getTableNames() {
        return tableNames;
    }
}
