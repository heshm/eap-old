package org.eap.stock.param.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.eap.framework.web.endpoint.BaseEndpoint;
import org.eap.stock.param.domain.Price;
import org.eap.stock.param.service.PriceService;

@RestController
@RequestMapping(value = "/stock/param/price")
public class PriceEndpoint extends BaseEndpoint {
	
	@Autowired
	private PriceService priceService;
	
	@GetMapping("/list")
	public List<Price> list(){
		return priceService.getAllPriceList();
	}

}
