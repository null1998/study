package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.annotation.ReplaceTable;
import org.example.entity.Region;

import java.util.List;

@ReplaceTable(busType = "type_A", tableName = "region")
@Mapper
public interface RegionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Region record);

    Region selectByPrimaryKey(Integer id);

    List<Region> selectAll();

    int updateByPrimaryKey(Region record);

    List<Region> selectByIds(@Param("idList") List<Integer> idList);

    int deleteByIds(@Param("idList") List<Integer> idList);
}