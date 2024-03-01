package org.example.config;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.example.annotation.ReplaceTable;
import org.example.common.CommonConst;
import org.example.entity.Table;
import org.example.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * @author huang
 */
@Configuration
public class TableCutoverConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableCutoverConfiguration.class);

    /**
     * 可传输的ThreadLocal，用于在父子线程上下文传递当前使用表名
     *
     * @return {@link ThreadLocal}<{@link Map}<{@link String}, {@link String}>>
     */
    @Bean("transmittableThreadLocal")
    public ThreadLocal<Map<String, String>> transmittableThreadLocal() {
        return new TransmittableThreadLocal<>();
    }

    /**
     * 业务线程池
     *
     * @return {@link Executor}
     */
    @Bean("businessThreadPool")
    public Executor businessThreadPool() {
        return ThreadUtil.getThreadPoolExecutor("thread-pool-");
    }

    /**
     * mybatis方法全限定名作为key，原表作为value的ConcurrentHashMap
     * 用于在mybatis拦截器中过滤方法和替换sql
     *
     * @return {@link Map}<{@link String}, {@link String}>
     * @throws Throwable 异常
     */
    @Bean("methodAndOriginTableMap")
    public Map<String, Table> methodAndOriginTableMap() throws Throwable {
        LocalDateTime time = LocalDateTime.now();
        Map<String, Table> methodAndOriginTableMap = new ConcurrentHashMap<>(32);
        // 扫描指定包下的所有类
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath*:org/example/**/*.class");
        CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        for (Resource resource : resources) {
            String className = readerFactory.getMetadataReader(resource).getClassMetadata().getClassName();
            Class<?> aClass = systemClassLoader.loadClass(className);
            // 只加载有@ReplaceTable注解的类
            if (aClass.isAnnotationPresent(ReplaceTable.class)) {
                ReplaceTable annotation = aClass.getAnnotation(ReplaceTable.class);
                for (Method declaredMethod : aClass.getDeclaredMethods()) {
                    // 获取方法全限定名
                    String methodName = declaredMethod.getDeclaringClass().getName() + "." + declaredMethod.getName();
                    Table originTable = new Table();
                    // 获取业务类型和原表名
                    originTable.setBusType(annotation.busType());
                    originTable.setTableName(annotation.tableName());
                    methodAndOriginTableMap.put(methodName, originTable);
                }
            }
        }
        LOGGER.info("表切换组件，加载mybatis方法个数：{}，耗时：{}毫秒", methodAndOriginTableMap.size(), ChronoUnit.MILLIS.between(time, LocalDateTime.now()));
        return methodAndOriginTableMap;
    }

    @Bean("businessProperties")
    public Properties businessProperties() {
        Properties properties = new Properties();
        properties.setProperty(CommonConst.LOGGER, CommonConst.OFF);
        return properties;
    }

    /**
     * 模拟数据库存储更在使用的表名
     *
     * @return {@link Map}<{@link String}, {@link String}>
     */
    @Bean("mockDataBaseMap")
    public Map<String, String> mockDataBaseMap() {
        Map<String, String> mockDataBaseMap= new ConcurrentHashMap<>(32);
        mockDataBaseMap.put("type_A", "region");
        mockDataBaseMap.put("type_B", "type_B_table");
        return mockDataBaseMap;
    }
}
