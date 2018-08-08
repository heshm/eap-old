package org.eap.stock.param.service;

import java.util.List;
import java.util.Map;

import org.eap.stock.param.dto.ItemDTO;

public interface ItemService {
	
	List<ItemDTO> readItemList(Map<String,Object> map);
	
	ItemDTO readItemById(String id);
	
	void createItem(ItemDTO itemDto);
	
	void updateItem(ItemDTO itemDto);

}
