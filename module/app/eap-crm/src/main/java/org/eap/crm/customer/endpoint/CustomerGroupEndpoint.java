package org.eap.crm.customer.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.eap.crm.customer.service.CustomerService;
import org.eap.crm.customer.dto.CustomerGroupDTO;
import org.eap.framework.web.endpoint.BaseEndpoint;
import org.eap.framework.web.util.WebConst;

@RestController
@RequestMapping(value = "/crm/customer/customerGroup")
public class CustomerGroupEndpoint extends BaseEndpoint {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/tree")
	public CustomerGroupDTO tree(){
		return customerService.getCustomerGroupTree(WebConst.ROOT);
	}
	
	@PutMapping("/update")
	public String update(@RequestBody CustomerGroupDTO dto){
		customerService.updateCustomerGroup(dto);
		return UPDATED;
	}
	
	@PostMapping("/create")
	public String create(@RequestBody CustomerGroupDTO dto){
		customerService.createCustomerGroup(dto);
		return CREATED;
	}

}
