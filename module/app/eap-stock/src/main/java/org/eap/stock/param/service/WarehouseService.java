package org.eap.stock.param.service;

import java.util.List;

import org.eap.stock.param.domain.Warehouse;

public interface WarehouseService {
	
	List<Warehouse> readAllWarehouseListWithoutChildren();

	List<Warehouse> readAllWarehouseListWithChildren();

	Warehouse getOneWarehouseWithChildren(String id);

}
