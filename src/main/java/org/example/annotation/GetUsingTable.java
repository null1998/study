package org.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 获取当前使用表名注解，标注在业务入口方法上
 *
 * @author huang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GetUsingTable {
    /**
     * 业务类型，根据该值获取当前使用表名
     *
     * @return {@link String[]}
     */
    String[] busType();
}
