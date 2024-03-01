package org.example.service;

import org.example.entity.Table;

import java.util.List;

/**
 * @author huang
 */
public interface ITableCutoverService {
    /**
     * 获取当前使用表名
     *
     * @param busTypeArr 业务类型数组
     * @return {@link List}<{@link Table}>
     */
    List<Table> getUsingTable(String[] busTypeArr);
}
