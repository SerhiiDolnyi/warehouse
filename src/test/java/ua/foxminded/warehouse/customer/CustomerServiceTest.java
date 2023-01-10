package ua.foxminded.warehouse.customer;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.warehouse.models.Address;
import ua.foxminded.warehouse.models.Customer;
import ua.foxminded.warehouse.repositories.CustomerRepository;
import ua.foxminded.warehouse.services.AddressService;
import ua.foxminded.warehouse.services.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    public void findByIdTest_ShouldReturnCustomer(){
        Customer customer = new Customer("Customer_Name", 3);
        when(customerRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(customer));
        assertEquals(customer, customerService.findById(Mockito.anyInt()));
        verify(customerRepository).findById(Mockito.anyInt());
    }

    @Test
    public void findAllTest_ShouldReturnAllCustomer(){
        Customer customer1 = new Customer("Customer_Name1", 3);
        Customer customer2 = new Customer("Customer_Name2", 3);
        List<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);
        when(customerRepository.findAll()).thenReturn(customers);
        assertEquals(customers, customerService.findAll());
        verify(customerRepository).findAll();
    }

    @Test
    public void saveCistomerTest_ShouldVerifySaveMethodOfCustomerRepository() {
        Customer customer = new Customer();
        customerService.save(customer);
        verify(customerRepository).save(customer);
    }

    @Test
    public void updateCustomerTest_ShouldVerifySaveMethodOfCustomerRepository() {
        Customer customer = new Customer();
        customerService.update(2, customer);
        verify(customerRepository).save(customer);
    }

    @Test
    public void deleteCustomerServiceTest_ShouldVerifyDeleteMethodOfCustomerRepository() {
        customerService.delete(1);
        verify(customerRepository).deleteById(1);
    }
}
