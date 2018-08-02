package org.eap.common.setup.mapper;

import org.eap.common.setup.domain.Department;
import java.util.List;

public interface DepartmentMapper {
    int deleteByPrimaryKey(String id);

    int insert(Department record);

    Department selectByPrimaryKey(String id);

    List<Department> selectAll();
    
    List<Department> selectChild(String parentId);

    int updateByPrimaryKey(Department record);
    
}