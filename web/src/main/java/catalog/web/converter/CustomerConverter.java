package catalog.web.converter;

import catalog.core.model.Customer;
import catalog.web.dto.CustomerDto;
import org.springframework.stereotype.Component;

@Component
public class CustomerConverter extends BaseConverter<Customer, CustomerDto> {
    @Override
    public Customer convertDtoToModel(CustomerDto dto) {
        Customer model = new Customer();
        model.setId(dto.getId());
        model.setPhoneNumber(dto.getPhoneNumber());
        model.setName(dto.getName());
        return model;
    }

    @Override
    public CustomerDto convertModelToDto(Customer customer) {
        CustomerDto dto = new CustomerDto(customer.getName(), customer.getPhoneNumber());
        dto.setId(customer.getId());
        return dto;
    }
}
