package catalog.web.controller;

import catalog.core.model.Customer;
import catalog.core.service.ICustomerService;
import catalog.web.converter.CustomerConverter;
import catalog.web.dto.CustomerDto;
import catalog.web.dto.CustomersDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {
    public static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private CustomerConverter customerConverter;


    @RequestMapping(value = "/customers")
    CustomersDto getAllCustomers() {
        log.trace("getAllCustomers --- method entered");
        CustomersDto c = new CustomersDto(
                customerConverter.convertModelsToDtos(
                        customerService.getAllCustomers()));
        log.trace("getAllCustomers - method finished: result={}", c);
        return c;
//        return new CustomersDto(
//                customerConverter.convertModelsToDtos(
//                        customerService.getAllCustomers()));
    }
//    @RequestMapping(value = "/customers")
//    List<CustomerDto> getAllCustomers() {
//        log.trace("getAllCustomers --- method entered");
//        List<Customer> c=customerService.getAllCustomers();
//
//        log.trace("getAllCustomers - method finished: result={}", c);
//        return new ArrayList<>(customerConverter.convertModelsToDtos(c));
////        return new CustomersDto(
////                customerConverter.convertModelsToDtos(
////                        customerService.getAllCustomers()));
//}

    @RequestMapping(value = "/customers/{name}")
    CustomersDto filterCustomers(@PathVariable String name) {
        log.trace("filterCustomers --- method entered, name = {}", name);
        CustomersDto c = new CustomersDto(
                customerConverter.convertModelsToDtos(
                        customerService.filterCustomersByName(name)));
        log.trace("filterCustomers - method finished:: result={}", c);
        return c;
//        return new CustomersDto(
//                customerConverter.convertModelsToDtos(
//                        customerService.filterCustomersByName(name)));
    }


    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    CustomerDto addCustomer(@RequestBody CustomerDto customerDto) {
        log.trace("addCustomer - method entered: customer={}", customerDto);

        Customer customer = customerConverter.convertDtoToModel(customerDto);

        Customer result = customerService.saveCustomer(customer);

        log.trace("addCustomer - method finished: result ={}", result);

        return customerConverter.convertModelToDto(result);
    }

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.PUT)
    CustomerDto updateCustomer(@PathVariable Long id, @RequestBody CustomerDto dto) {
        log.trace("updateCustomer - method entered: customer={}", dto);
        Long getId = id;
        CustomerDto c = customerConverter.convertModelToDto(
                customerService.updateCustomer(
                        customerConverter.convertDtoToModel(dto)
                ));
        log.trace("updateCustomer - method finished: result ={}", c);

        return c;
//        return
//                customerConverter.convertModelToDto(
//                        customerService.updateCustomer(
//                                customerConverter.convertDtoToModel(dto)
//                        ));
    }

    @RequestMapping(value = "/customers/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        log.trace("deleteCustomer - method entered: id={}", id);

        customerService.deleteCustomer(id);
        log.trace("deleteCustomer - method finished");

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
