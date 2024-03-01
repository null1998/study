package org.example.entity;

/**
 * 表
 *
 * @author huang
 */
public class Table {
    /**
     * 业务类型
     */
    private String busType;
    /**
     * 表名
     */
    private String tableName;

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
