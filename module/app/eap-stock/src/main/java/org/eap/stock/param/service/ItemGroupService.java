package org.eap.stock.param.service;

import java.util.List;

import org.eap.stock.param.domain.ItemGroup;

public interface ItemGroupService {
	
	ItemGroup readAllItemGroup(String id);
	
	List<ItemGroup> readItemGroupList(String id);
	
	List<ItemGroup> readChildItemGroup(String id);
	
	ItemGroup readOneItemGroup(String id);
	
	void updateItemGroup(ItemGroup record);
	
	void addItemGroup(ItemGroup record);
	
	void deleteItemGroup(String id);

}
