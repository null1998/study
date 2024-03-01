package org.example.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.entity.Table;
import org.example.service.ITableCutoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author huang
 */
@Service
public class TableCutoverServiceImpl implements ITableCutoverService {
    private Map<String, String> map;

    @Autowired
    public TableCutoverServiceImpl(@Qualifier("mockDataBaseMap") Map<String, String> map) {
        this.map = map;
    }

    @Override
    public List<Table> getUsingTable(String[] busTypeArr) {
        List<Table> tableList = new ArrayList<>();
        if (busTypeArr != null) {
            for (String busType : busTypeArr) {
                String tableName = map.get(busType);
                if (StringUtils.isNotBlank(tableName)) {
                    Table table = new Table();
                    table.setBusType(busType);
                    table.setTableName(tableName);
                    tableList.add(table);
                }
            }
        }
        return tableList;
    }
}
