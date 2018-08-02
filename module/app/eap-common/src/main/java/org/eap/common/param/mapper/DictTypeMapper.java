package org.eap.common.param.mapper;

import org.eap.common.param.domain.DictType;
import java.util.List;

public interface DictTypeMapper {
    int deleteByPrimaryKey(String id);

    int insert(DictType record);

    DictType selectByPrimaryKey(String id);

    List<DictType> selectAll();

    int updateByPrimaryKey(DictType record);
}