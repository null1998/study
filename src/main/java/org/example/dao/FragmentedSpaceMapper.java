package org.example.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author huang
 */
@Mapper
public interface FragmentedSpaceMapper {
    void alterTableEngine(@Param("tableName") String tableName);
    void enableRowMovement(@Param("tableName") String tableName);
    void shrinkSpaceCascade(@Param("tableName") String tableName);
    void disableRowMovement(@Param("tableName") String tableName);
    void vacuumFull(@Param("tableName") String tableName);
}
