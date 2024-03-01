package org.example.component;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.example.common.CommonConst;
import org.example.entity.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * mybatis拦截器
 *
 * @author huang
 */
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class BusinessMybatisInterceptor implements Interceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessMybatisInterceptor.class);

    private static final String QUERY_METHOD = "query";

    private static final String UPDATE_METHOD = "update";

    private static final Integer QUERY_METHOD_ARGS_NUM_FOUR = 4;

    private static final Integer QUERY_METHOD_ARGS_NUM_SIX = 6;

    private Map<String, Table> map;

    private ThreadLocal<Map<String, String>> threadLocal;

    private Properties properties;

    @Autowired
    public BusinessMybatisInterceptor(@Qualifier("methodAndOriginTableMap") Map<String, Table> map,
                                      @Qualifier("transmittableThreadLocal") ThreadLocal<Map<String, String>> threadLocal,
                                      @Qualifier("businessProperties") Properties properties) {
        this.map = map;
        this.threadLocal = threadLocal;
        this.properties = properties;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Table originTable = map.get(mappedStatement.getId());
        // 只拦截有@ReplaceTable注解的mybatis类
        if (originTable != null) {
            Object parameter = args[1];
            BoundSql boundSql;
            // query方法，拦截select语句
            if (QUERY_METHOD.equals(invocation.getMethod().getName())) {
                // 4个参数的query方法
                if (args.length == QUERY_METHOD_ARGS_NUM_FOUR) {
                    boundSql = mappedStatement.getBoundSql(parameter);
                    // 替换sql中的表名
                    mappedStatement = replaceTable(mappedStatement, boundSql, originTable);
                    args[0] = mappedStatement;
                }
                // 6个参数的query方法
                if (args.length == QUERY_METHOD_ARGS_NUM_SIX) {
                    boundSql = (BoundSql) args[5];
                    // 替换sql中的表名
                    replaceTable(boundSql, originTable);
                    args[5] = boundSql;
                }
            }
            // update方法，拦截update，delete，insert语句
            if (UPDATE_METHOD.equals(invocation.getMethod().getName())) {
                boundSql = mappedStatement.getBoundSql(parameter);
                // 替换sql中的表名
                mappedStatement = replaceTable(mappedStatement, boundSql, originTable);
                args[0] = mappedStatement;
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

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

    /**
     * 使用反射修改boundSql中的表名
     *
     * @param boundSql    sql语句的对象
     * @param originTable 原表
     * @return 是否替换了sql
     * @throws Throwable 异常
     */
    private boolean replaceTable(BoundSql boundSql, Table originTable) throws Throwable {
        Map<String, String> usingTableMap = threadLocal.get();
        // 没有获取到当前使用表，不执行替换
        if (MapUtils.isEmpty(usingTableMap)) {
            return false;
        }
        // 原sql
        String sql = boundSql.getSql();
        // 原表名
        String originTableName = originTable.getTableName();
        // 当前使用表名
        String usingTableName = usingTableMap.get(originTable.getBusType());
        // 原表名或当前使用表名为空时，不执行替换
        if (StringUtils.isBlank(originTableName) || StringUtils.isBlank(usingTableName)) {
            return false;
        }
        Field field = boundSql.getClass().getDeclaredField("sql");
        field.setAccessible(true);
        field.set(boundSql, replace(sql, originTableName, usingTableName));
        if (Objects.equals(properties.getProperty(CommonConst.LOGGER), CommonConst.ON)) {
            LOGGER.info("执行表名替换：{} -> {}，\nmybatis原sql：{}，\n替换表名后的sql：{}", originTableName, usingTableName, sql, boundSql.getSql());
        }
        return true;
    }

    /**
     * 替换sql
     *
     * @param sql             原sql
     * @param originTableName 原表名
     * @param usingTableName  当前使用表名
     * @return {@link String}
     */
    private String replace(String sql, String originTableName, String usingTableName) {
        // 若包含原表名，直接替换
        if (sql.contains(originTableName)) {
            return sql.replace(originTableName, usingTableName);
        }
        // 转换为大写后再替换
        return sql.toUpperCase().replace(originTableName.toUpperCase(), usingTableName);
    }

    /**
     * 复制mappedStatement并替换boundSql
     *
     * @param mappedStatement mapper方法
     * @param boundSql        sql语句的对象
     * @param originTable     原表
     * @return {@link MappedStatement}
     * @throws Throwable 异常
     */
    private MappedStatement replaceTable(MappedStatement mappedStatement, BoundSql boundSql, Table originTable) throws Throwable {
        // 使用反射修改boundSql中的表名
        boolean replaceResult = replaceTable(boundSql, originTable);
        // 如果没有替换sql，原样返回
        if (!replaceResult) {
            return mappedStatement;
        }
        // 复制MappedStatement并替换boundSql
        MappedStatement.Builder builder = new MappedStatement.Builder(
                mappedStatement.getConfiguration(),
                mappedStatement.getId(),
                // 替换boundSql
                new BoundSqlSqlSource(boundSql),
                mappedStatement.getSqlCommandType());
        builder.resource(mappedStatement.getResource());
        builder.fetchSize(mappedStatement.getFetchSize());
        builder.statementType(mappedStatement.getStatementType());
        builder.keyGenerator(mappedStatement.getKeyGenerator());
        if (mappedStatement.getKeyProperties() != null && mappedStatement.getKeyProperties().length > 0) {
            builder.keyProperty(String.join(",", mappedStatement.getKeyProperties()));
        }
        builder.timeout(mappedStatement.getTimeout());
        builder.parameterMap(mappedStatement.getParameterMap());
        builder.resultMaps(mappedStatement.getResultMaps());
        builder.resultSetType(mappedStatement.getResultSetType());
        builder.cache(mappedStatement.getCache());
        builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
        builder.useCache(mappedStatement.isUseCache());
        return builder.build();
    }
}
