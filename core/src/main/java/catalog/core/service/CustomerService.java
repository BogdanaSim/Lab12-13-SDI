package catalog.core.service;

import catalog.core.model.Customer;
import catalog.core.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service("customerService")
public class CustomerService implements ICustomerService {

    public static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Returns all customers whose name contain the given string.
     *
     * @param filterString string to filter names by
     * @return customers whose name contain the given string
     */
    public Set<Customer> filterCustomersByName(String filterString) {
        log.trace("filterCustomersByName - method entered: filterName={}", filterString);
//        Iterable<Customer> entities = customerRepository.findAll();
//
//        Set<Customer> filteredCustomers = new HashSet<>();
//        entities.forEach(filteredCustomers::add);
//        filteredCustomers.removeIf(entity -> !entity.getName().contains(filterString));

//        List<Customer> filteredCustomers=customerRepository.findByNameContaining(filterString);
        List<Customer> filteredCustomers=customerRepository.findByNameContainingOrderByNameAsc(filterString);
        Set<Customer> res = new HashSet<>(filteredCustomers);
        log.trace("filterCustomersByName - method finished");
        return res;

    }

    @Override
    public List<Customer> getAllCustomers() {
        log.trace("getAllCustomers --- method entered");

        List<Customer> result = customerRepository.findAllByOrderByNameAsc();

        log.trace("getAllCustomers: result={}", result);

        return result;
    }

    @Override
    @Transactional
    public Customer saveCustomer(Customer entity) {
        boolean found = false;
        Customer res=entity;
        log.trace("saveCustomer - method entered: customer={}", entity);

//        List<Customer> customers = this.customerRepository.findAll();
//        for (Customer customer : customers) {
//            if (customer.getId().equals(entity.getId())) {
//                log.debug("saveCustomer - failed since customer with id " + entity.getId() + " already exists!");
//                found = true;
//            }
//        }
//        if (!found) {
//
//            res= this.customerRepository.save(entity);
//
//        }
        long id = 0;
        for (Customer customer : this.customerRepository.findAll())
            id = Math.max(id, customer.getId() + 1);
        Customer customerToBeAdded = new Customer(entity.getName(),entity.getPhoneNumber());
        customerToBeAdded.setId(id);
        customerRepository.save(customerToBeAdded);
        log.trace("saveCustomer - method finished");
        return res;

    }

    @Override
    @Transactional
    public Customer updateCustomer(Customer entity) {
        log.trace("updateCustomer - method entered: customer={}", entity);

        customerRepository.findById(entity.getId())
                .ifPresent(a -> {
                    a.setName(entity.getName());
                    a.setPhoneNumber(entity.getPhoneNumber());
                    log.debug("updateCustomer - method entered: customer={}", a);
                });

        log.trace("updateCustomer - method finished");
        return customerRepository.findById(entity.getId()).get();
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        log.trace("deleteCustomer - method entered: id={}", id);
        customerRepository.deleteById(id);
        log.trace("deleteCustomer - method finished");
    }
}