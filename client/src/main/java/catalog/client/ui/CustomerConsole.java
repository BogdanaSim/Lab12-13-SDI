package catalog.client.ui;

import catalog.core.exceptions.ValidatorException;
import catalog.core.model.Customer;
import catalog.core.model.Customer;
import catalog.core.validators.CustomerValidator;
import catalog.web.dto.CustomersDto;
import catalog.web.dto.CustomerDto;
import catalog.web.dto.CustomersDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Service
public class CustomerConsole extends BaseConsole {
    public static final Logger log = LoggerFactory.getLogger(CustomerConsole.class);
    @Autowired
    ExecutorService executorService;
    @Autowired
    RestTemplate restTemplate;

    static final String EXIT_KEY = "0";
    static final String ADD_KEY = "1";
    static final String PRINT_KEY = "2";
    static final String REMOVE_KEY = "3";
    static final String UPDATE_KEY = "4";
    static final String FILTER_KEY = "5";

    private CustomerValidator customerValidator;

    @PostConstruct
    public void init() {
        this.customerValidator = new CustomerValidator();
        HashMap<String, Method> functionalityMap = null;
        try {
            functionalityMap = new HashMap<>() {{
                put(EXIT_KEY, CustomerConsole.class.getMethod("exit"));
                put(ADD_KEY, CustomerConsole.class.getMethod("addCustomer"));
                put(PRINT_KEY, CustomerConsole.class.getMethod("printAllCustomers"));
                put(REMOVE_KEY, CustomerConsole.class.getMethod("removeCustomer"));
                put(UPDATE_KEY, CustomerConsole.class.getMethod("updateCustomer"));
                put(FILTER_KEY, CustomerConsole.class.getMethod("filterCustomers"));
            }};
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.setFunctionalityMap(functionalityMap);
    }

    @Override
    protected void printMenu() {
        System.out.println(
                "\nCUSTOMER SUBMENU\n" +
                        String.format("%s. Exit\n", EXIT_KEY) +
                        String.format("%s. Add\n", ADD_KEY) +
                        String.format("%s. Print all\n", PRINT_KEY) +
                        String.format("%s. Remove\n", REMOVE_KEY) +
                        String.format("%s. Update\n", UPDATE_KEY) +
                        String.format("%s. Filter by name\n", FILTER_KEY)
        );
    }

    protected Customer readEntity() {
        System.out.println("Read customer {id, name, phoneNumber}\n");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        Long id = null;
        String name = null;
        String phoneNumber = null;
        try {
            System.out.print("Customer ID: ");
            id = Long.valueOf(bufferRead.readLine());
            System.out.print("Customer Name: ");
            name = bufferRead.readLine();
            System.out.print("Customer Phone Number: ");
            phoneNumber = bufferRead.readLine();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());

            // Unrecoverable from, so exit
            this.exit();
        }

        Customer customer = new Customer(name, phoneNumber);
        customer.setId(id);

        return customer;
    }


    protected Customer createEntity(Long id) {
        Customer customer = new Customer("aaaa", "1234");
        customer.setId(id);
        return customer;
    }

    public void addCustomer() {
        log.trace("addCustomer - method entered.");
        Customer entity = this.readEntity();
        try {
            customerValidator.validate(entity);
            //return CompletableFuture.supplyAsync(() -> {
            try {
                String url = "http://localhost:8080/api/customers";
                CustomerDto adto = new CustomerDto(entity.getName(), entity.getPhoneNumber());
                adto.setId(entity.getId());
                restTemplate.postForObject(url, adto, CustomerDto.class);
                System.out.println("Customer successfully updated");

            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Inaccessible server");
            }
        } catch (ValidatorException ex) {
            System.out.println(ex.getMessage());
        }
        log.trace("addCustomer - method finished");
        // },executorService);
    }

    public void printAllCustomers() {
        log.trace("printAllCustomers- method entered.");
        try {
            String url = "http://localhost:8080/api/customers";
            CustomersDto customers = restTemplate.getForObject(url, CustomersDto.class);
            if (customers == null)
                System.out.println("Could not retrieve customers from server");
            else {
                Set<Customer> customers1 = customers.getCustomers().stream().map(customerDto -> {
                    Customer c = new Customer(customerDto.getName(), customerDto.getPhoneNumber());
                    c.setId(customerDto.getId());
                    return c;
                }).collect(Collectors.toSet());
                for (Customer customer : customers1) {
                    System.out.println(customer.toString());
                }
            }
        } catch (ResourceAccessException resourceAccessException) {
            System.out.println("Inaccessible server");
        }
        log.trace("printAllCustomers - method finished");
    }

    public void removeCustomer() {
        log.trace("removeCustomer - method entered.");
        Long id = this.readID();
        if (id > 0) {
            try {
                String url = "http://localhost:8080/api/customers";
                restTemplate.delete(url + "/{id}", id);
                System.out.println("Customer successfully deleted");
            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Inaccessible server");
            }
        } else {
            System.out.println("Invalid id!");
        }
        log.trace("removeCustomer - method finished");
    }

    public void updateCustomer() {
        log.trace("updateCustomer - method entered.");
        Customer entity = this.readEntity();
        try {
            customerValidator.validate(entity);
            try {
                String url = "http://localhost:8080/api/customers";
                CustomerDto animalToUpdate = new CustomerDto(entity.getName(), entity.getPhoneNumber());
                animalToUpdate.setId(entity.getId());
                restTemplate.put(url + "/{id}", animalToUpdate, animalToUpdate.getId());
                System.out.println("Customer successfully updated");
            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Inaccessible server");
            }
        } catch (ValidatorException ex) {
            System.out.println(ex.getMessage());
        }
        log.trace("updateCustomer - method finished");
    }

    public void filterCustomers() {
        log.trace("filterCustomers - method entered.");
        long id = 1;
        Customer customer = createEntity(id);
        String filterString = "";
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Customer Name: ");
            filterString = bufferRead.readLine();
            customer.setName(filterString);
            try{
                CustomerValidator.validateName(filterString);
            //animal.setAge(ageToFilterBy);
//            Set<Animal> filtered = ((AnimalService) this.service).filterCustomersByAgeLessThan(ageToFilterBy);
//            filtered.forEach(System.out::println);
            try {
                String url = "http://localhost:8080/api/customers";
                CustomersDto customers = restTemplate.getForObject(url + "/" + filterString, CustomersDto.class);
                if (customers == null)
                    System.out.println("Could not retrieve customers from server");
                else {
                    Set<Customer> customers1 = customers.getCustomers().stream().map(customerDto -> {
                        Customer c = new Customer(customerDto.getName(), customerDto.getPhoneNumber());
                        c.setId(customerDto.getId());
                        return c;
                    }).collect(Collectors.toSet());
                    for (Customer customere : customers1) {
                        System.out.println(customere.toString());
                    }
                    //System.out.println(customers.getCustomers());
                }
            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Inaccessible server");
            }}catch (ValidatorException ex){
                System.out.println(ex.getMessage());
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());

            // Unrecoverable from, so exit
            this.exit();
        }
        log.trace("filterCustomers - method finished.");
    }
}
