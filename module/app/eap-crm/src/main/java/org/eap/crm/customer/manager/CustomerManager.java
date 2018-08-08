package org.eap.crm.customer.manager;

import org.eap.crm.customer.domain.Customer;

/**
 * Created by Lenovo on 2017/6/20.
 */
public interface CustomerManager {
    Customer getOneCustomerById(String customerId);
}
