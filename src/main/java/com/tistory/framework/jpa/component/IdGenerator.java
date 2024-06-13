package com.tistory.framework.jpa.component;

import com.tistory.framework.jpa.service.TableService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class IdGenerator {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TableService tableService;

    @Transactional
    public synchronized String generateId(String tableName) {
        if (!isValidTableName(tableName)) {
            throw new IllegalArgumentException("Invalid table name: " + tableName);
        }

        String idPrefix = getIdPrefix(tableName);
        String datePrefix = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = idPrefix + datePrefix;

        String queryStr = String.format(
                "SELECT IFNULL(MAX(CAST(SUBSTRING(id, LENGTH(:prefix) + 1) AS UNSIGNED)), 0) + 1 " +
                        "FROM %s " +
                        "WHERE id LIKE :prefixPattern",
                tableName
        );

        Query query = entityManager.createNativeQuery(queryStr);
        query.setParameter("prefix", prefix);
        query.setParameter("prefixPattern", prefix + "%");

        int newIdSuffix = ((Number) query.getSingleResult()).intValue();
        String newId = String.format("%s%06d", prefix, newIdSuffix);

        return newId;
    }

    private String getIdPrefix(String tableName) {
        switch (tableName) {
            case "user":
                return "U";
            default:
                throw new IllegalArgumentException("Invalid table name: " + tableName);
        }
    }

    private boolean isValidTableName(String tableName) {
        return tableService.getTableNames().contains(tableName);
    }
}