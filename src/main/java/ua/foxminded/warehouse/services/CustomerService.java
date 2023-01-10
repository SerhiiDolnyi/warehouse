package ua.foxminded.warehouse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.warehouse.models.Customer;
import ua.foxminded.warehouse.repositories.CustomerRepository;
import ua.foxminded.warehouse.util.exception.customer.CustomerNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CustomerService {
    CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    public Customer findById(int customerId){
        Optional<Customer> foundCustomer = customerRepository.findById(customerId);
        return foundCustomer.orElseThrow(CustomerNotFoundException::new);
    }

    public Customer findByName(String name) {
        Optional<Customer> foundCustomer = customerRepository.findCustomerByName(name);
        return foundCustomer.orElseThrow(CustomerNotFoundException::new);
    }

    @Transactional
    public void save (Customer customer){
        customerRepository.save(customer);
    }

    @Transactional
    public void update (int customerId, Customer updatedCustomer){
        updatedCustomer.setId(customerId);
        customerRepository.save(updatedCustomer);
    }

    @Transactional
    public void delete (int customerId){
        customerRepository.deleteById(customerId);
    }
}
