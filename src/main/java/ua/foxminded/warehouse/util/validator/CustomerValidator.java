package ua.foxminded.warehouse.util.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.warehouse.dto.CustomerDTO;
import ua.foxminded.warehouse.models.Customer;
import ua.foxminded.warehouse.repositories.CustomerRepository;

@Component
public class CustomerValidator implements Validator {
    private CustomerRepository customerRepository;

    @Autowired
    public CustomerValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Customer.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerDTO customerDTO = (CustomerDTO) target;
        if(customerRepository.findCustomerByName(customerDTO.getName()).isPresent()){
            errors.rejectValue("name", "", "This customer name already exists");
        }
    }
}
