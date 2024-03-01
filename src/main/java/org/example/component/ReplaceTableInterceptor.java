package org.example.component;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * @author huang
 */
//@Component
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class ReplaceTableInterceptor implements Interceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReplaceTableInterceptor.class);
    private final static Map<String,String> TABLE_MAP = new LinkedHashMap<>();
    static {
        TABLE_MAP.put("UNE_PAYBOOK_REPORT_SUPERVISE","UNE_PAYBOOK_REPORT_SUPERVISE_123456");
        TABLE_MAP.put("REGION","REGION_123456");
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        //获取MappedStatement对象
        MappedStatement ms = (MappedStatement) args[0];
        //获取传入sql语句的参数对象
        Object parameterObject = args[1];

        BoundSql boundSql;
        //由于逻辑关系，只会进入一次
        if (args.length == 4 || args.length == 2) {
            //4 个参数时
            boundSql = ms.getBoundSql(parameterObject);
        } else {
            //6 个参数时
            boundSql = (BoundSql) args[5];
        }
        //获取到拥有占位符的sql语句
        String sql = boundSql.getSql();
        LOGGER.info("拦截前sql :{}", sql);


        //判断是否需要替换表名
        if(isReplaceTableName(sql)){
            TableNameVisitor visitor = new TableNameVisitor(TABLE_MAP);
            List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, JdbcConstants.HIVE);
            for (SQLStatement stmt : stmtList) {
                stmt.accept(visitor);
            }
            sql = SQLUtils.toSQLString(stmtList, JdbcConstants.HIVE);
            LOGGER.info("拦截后sql :{}", sql);

            //重新生成一个BoundSql对象
            BoundSql bs = new BoundSql(ms.getConfiguration(),sql,boundSql.getParameterMappings(),parameterObject);

            if (args.length == 4 || args.length == 2) {
                //重新生成一个MappedStatement对象
                MappedStatement newMs = copyMappedStatement(ms, new BoundSqlSqlSource(bs));

                //赋回给实际执行方法所需的参数中
                args[0] = newMs;
            } else {
                args[5] = bs;
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /***
     * 判断是否需要替换表名
     * @param sql
     * @return
     */
    private boolean isReplaceTableName(String sql){
        for(String tableName : TABLE_MAP.keySet()){
            if(sql.toUpperCase().contains(tableName)){
                return true;
            }
        }
        return false;
    }

    /***
     * 复制一个新的MappedStatement
     * @param ms
     * @param newSqlSource
     * @return
     */
    private MappedStatement copyMappedStatement (MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(String.join(",",ms.getKeyProperties()));
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    /***
     * MappedStatement构造器接受的是SqlSource
     * 实现SqlSource接口，将BoundSql封装进去
     */
    public static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;
        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }
        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
