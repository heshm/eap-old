package org.eap.crm.customer.mapper;

import java.util.List;

import org.eap.crm.customer.domain.CustomerGroup;

public interface CustomerGroupMapper {
    int deleteByPrimaryKey(String id);

    int insert(CustomerGroup record);

    CustomerGroup selectByPrimaryKey(String id);

    List<CustomerGroup> selectAll();
    
    List<CustomerGroup> selectChildren(String parentId);

    int updateByPrimaryKey(CustomerGroup record);
    
    String selectCustomerGroupName(String id);
}