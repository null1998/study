package org.example.component;

import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveASTVisitorAdapter;

import java.util.Map;

/**
 * @author huang
 */
public class TableNameVisitor extends HiveASTVisitorAdapter {
    private Map<String,String> tableNameMap;

    public TableNameVisitor(Map<String, String> tableNameMap) {
        this.tableNameMap = tableNameMap;
    }

    @Override
    public boolean visit(SQLExprTableSource x) {
        if (tableNameMap.get(x.getExpr().toString().toUpperCase()) != null) {
            x.setExpr(tableNameMap.get(x.getExpr().toString().toUpperCase()));
        }
        return true;
    }
}
