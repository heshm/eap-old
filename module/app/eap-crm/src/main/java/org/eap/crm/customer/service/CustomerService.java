package org.eap.crm.customer.service;

import java.util.List;

import org.eap.crm.customer.dto.CustomerGroupDTO;

public interface CustomerService {
	
	CustomerGroupDTO getCustomerGroupTree(String id);
	
	List<CustomerGroupDTO> getCustomerGroupTreeList(String id);
	
	void updateCustomerGroup(CustomerGroupDTO dto);
	
	void createCustomerGroup(CustomerGroupDTO dto);

}
