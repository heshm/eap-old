package org.eap.common.param.mapper;

import org.eap.common.param.domain.Area;
import java.util.List;

public interface AreaMapper {
    int deleteByPrimaryKey(String id);

    int insert(Area record);

    Area selectByPrimaryKey(String id);

    List<Area> selectAll();
    
    List<Area> selectChild(String parentId);

    int updateByPrimaryKey(Area record);
    
    String selectAreaNameByCode(String id);
   
}