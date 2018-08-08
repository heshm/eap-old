package org.eap.stock.param.service;

import java.util.List;

import org.eap.stock.param.domain.Price;


public interface PriceService {
	
	List<Price> getAllPriceList();
	
	List<Price> getEnablePriceList();
	
	void save(Price price);
	
	Price getOnePrice(String id);

}
