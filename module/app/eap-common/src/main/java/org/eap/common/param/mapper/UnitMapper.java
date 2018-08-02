package org.eap.common.param.mapper;

import java.util.List;

import org.springframework.data.domain.Pageable;

import org.eap.common.param.domain.Unit;

public interface UnitMapper {
    int deleteByPrimaryKey(String id);

    int insert(Unit record);

    Unit selectByPrimaryKey(String id);

    List<Unit> selectAll();
    
    List<Unit> selectAll(Pageable pageable);

    int updateByPrimaryKey(Unit record);
    
    int countByExample();
}