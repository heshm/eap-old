package org.eap.oa.act.mapper;

import org.eap.oa.act.domain.ActModelXref;
import java.util.List;

public interface ActModelXrefMapper {
    int deleteByPrimaryKey(String id);

    int insert(ActModelXref record);

    ActModelXref selectByPrimaryKey(String id);

    List<ActModelXref> selectAll();

    int updateByPrimaryKey(ActModelXref record);
    
    ActModelXref selectByModelId(String modelId);
}