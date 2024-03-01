package org.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 替换表名注解，标注在mybatis类名上
 *
 * @author huang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ReplaceTable {
    /**
     * 业务类型
     *
     * @return {@link String}
     */
    String busType();
    /**
     * 原表名
     *
     * @return {@link String}
     */
    String tableName();
}
