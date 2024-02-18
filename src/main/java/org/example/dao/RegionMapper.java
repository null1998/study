package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.entity.Region;

import java.util.List;

@Mapper
public interface RegionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Region record);

    Region selectByPrimaryKey(Integer id);

    List<Region> selectAll();

    int updateByPrimaryKey(Region record);

    List<Region> selectByIds(@Param("idList") List<Integer> idList);
}