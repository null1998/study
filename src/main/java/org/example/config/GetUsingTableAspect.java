package org.example.config;

import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.annotation.GetUsingTable;
import org.example.common.CommonConst;
import org.example.component.AsyncRequestTimeoutHandler;
import org.example.entity.Table;
import org.example.service.ITableCutoverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author huang
 */
@Aspect
@Component
public class GetUsingTableAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncRequestTimeoutHandler.class);

    private ITableCutoverService tableCutoverService;

    private ThreadLocal<Map<String, String>> threadLocal;

    private Properties properties;

    @Autowired
    public GetUsingTableAspect(ITableCutoverService tableCutoverService,
                               @Qualifier("transmittableThreadLocal") ThreadLocal<Map<String, String>> threadLocal,
                               @Qualifier("businessProperties") Properties properties) {
        this.tableCutoverService = tableCutoverService;
        this.threadLocal = threadLocal;
        this.properties = properties;
    }

    @Around("@annotation(org.example.annotation.GetUsingTable)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        GetUsingTable annotation = method.getAnnotation(GetUsingTable.class);
        String[] busTypeArr = annotation.busType();
        List<Table> usingTableList = tableCutoverService.getUsingTable(busTypeArr);
        if (CollectionUtils.isEmpty(usingTableList)) {
            return joinPoint.proceed();
        }
        Map<String, String> usingTableMap = new ConcurrentHashMap<>(8);
        for (Table usingTable : usingTableList) {
            usingTableMap.put(usingTable.getBusType(), usingTable.getTableName());
        }
        threadLocal.set(usingTableMap);
        if (Objects.equals(properties.getProperty(CommonConst.LOGGER), CommonConst.ON)) {
            LOGGER.info("开始业务方法：{}，根据业务类型获取当前使用表名：{}", method.getDeclaringClass().getName() + "." + method.getName(), usingTableMap);
        }
        Object proceed = joinPoint.proceed();
        threadLocal.remove();
        if (Objects.equals(properties.getProperty(CommonConst.LOGGER), CommonConst.ON)) {
            LOGGER.info("结束业务方法：{}，移除当前使用表名：{}", method.getDeclaringClass().getName() + "." + method.getName(), usingTableMap);
        }
        return proceed;
    }
}
