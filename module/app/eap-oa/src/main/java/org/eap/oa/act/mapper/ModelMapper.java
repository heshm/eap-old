package org.eap.oa.act.mapper;

import org.eap.oa.act.domain.Model;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Sort;

public interface ModelMapper {
    int deleteByPrimaryKey(String id);

    int insert(Model record);

    Model selectByPrimaryKey(String id);
    
    Model selectByModelKey(String modelKey);
    
    List<Model> selectModelList(@Param("userId")String userId,@Param("modelType")Integer modelType,
    		@Param("filterText")String filterText,@Param("sort")Sort sort);

    List<Model> selectAll();

    int updateByPrimaryKey(Model record);
}