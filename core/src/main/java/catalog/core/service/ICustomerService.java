package catalog.core.service;

import catalog.core.model.Customer;

import java.util.List;
import java.util.Set;

public interface ICustomerService {
    List<Customer> getAllCustomers();

    Customer saveCustomer(Customer entity);

    Customer updateCustomer(Customer entity);

    void deleteCustomer(Long id);

    Set<Customer> filterCustomersByName(String name);

}
