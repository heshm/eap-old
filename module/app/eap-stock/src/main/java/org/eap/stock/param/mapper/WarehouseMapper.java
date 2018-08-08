package org.eap.stock.param.mapper;

import java.util.List;

import org.eap.stock.param.domain.Warehouse;

public interface WarehouseMapper {
    int deleteByPrimaryKey(String id);

    int insert(Warehouse record);

    Warehouse selectByPrimaryKey(String id);

    List<Warehouse> selectAll();

    int updateByPrimaryKey(Warehouse record);
}