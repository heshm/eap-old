package org.eap.common.setup.mapper;

import org.eap.common.setup.domain.DepartmentUserXref;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DepartmentUserXrefMapper {
    int deleteByPrimaryKey(@Param("departmentId") String departmentId, @Param("userId") String userId);
    
    int deleteByUserId(@Param("userId") String userId);

    int insert(DepartmentUserXref record);

    DepartmentUserXref selectByPrimaryKey(@Param("departmentId") String departmentId, @Param("userId") String userId);

    List<DepartmentUserXref> selectAll();

    int updateByPrimaryKey(DepartmentUserXref record);
    
    List<DepartmentUserXref> selectList(@Param("departmentId") String departmentId, @Param("userId") String userId);
}