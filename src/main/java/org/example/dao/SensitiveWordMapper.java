package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.entity.SensitiveWord;

import java.util.List;

/**
 * 敏感词配置dao
 *
 * @author huang
 */
@Mapper
public interface SensitiveWordMapper {

    int save(@Param("sensitiveWordList") List<SensitiveWord> sensitiveWordList);

    List<SensitiveWord> query(@Param("type") Integer type);

    int delete(@Param("idList") List<String> idList);
}
