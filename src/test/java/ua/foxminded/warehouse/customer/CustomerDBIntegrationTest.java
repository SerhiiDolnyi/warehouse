package ua.foxminded.warehouse.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import ua.foxminded.warehouse.models.Customer;
import ua.foxminded.warehouse.repositories.CustomerRepository;
import ua.foxminded.warehouse.services.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class CustomerDBIntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Value("${sql.script.create.address}")
    private String sqlAddAddress;

    @Value("${sql.script.create.address3}")
    private String sqlAddAddress3;

    @Value("${sql.script.create.customer2}")
    private String sqlAddCustomer2;

    @Value("${sql.script.create.customer3}")
    private String sqlAddCustomer3;

    @Value("${sql.script.delete.address}")
    private String sqlDeleteAddresses;

    @Value("${sql.script.delete.customer}")
    private String sqlDeleteCustomer;

    @BeforeEach
    public void setupDatabase() {
        jdbcTemplate.execute(sqlAddAddress);
        jdbcTemplate.execute(sqlAddCustomer2);
    }

    @AfterEach
    public void setupAfterTransaction(){
        jdbcTemplate.execute(sqlDeleteCustomer);
        jdbcTemplate.execute(sqlDeleteAddresses);
    }

    @Test
    public void findAllCustomer_ShouldReturnSizeOfList() {
        jdbcTemplate.execute(sqlAddAddress3);
        jdbcTemplate.execute(sqlAddCustomer3);
        List<Customer> customers = customerService.findAll();
        assertEquals(2, customers.size());
    }

    @ParameterizedTest
    @CsvSource({"0, 2", "1, 3"})
    public void findAllCustomer_ShouldReturnCustomerRate(int index, int customerRate) {
        jdbcTemplate.execute(sqlAddAddress3);
        jdbcTemplate.execute(sqlAddCustomer3);
        List<Customer> customers = customerService.findAll();
        Customer customer = customers.get(index);
        assertEquals(customerRate, customer.getCustomerRate());
    }

    @Test
    public void findCustomerByIdTest_ShouldReturnNameOfFoundCustomer() {
        Customer foundCustomer = customerService.findById(2);
        assertEquals("test2_customerName", foundCustomer.getName());
    }

    @Test
    public void saveCustomerTestShouldReturnNameOfSavedCustomer() {
        Customer customer = new Customer("Saved_Customer_Name", 3);
        customerService.save(customer);
        Customer savedCustomer = customerRepository.findCustomerByName("Saved_Customer_Name").get();
        assertEquals("Saved_Customer_Name", savedCustomer.getName());
    }

    @Test
    public void deleteCustomerTest(){
        Optional<Customer> deletedCustomer = customerRepository.findById(2);
        assertTrue(deletedCustomer.isPresent());
        customerService.delete(2);
        deletedCustomer =customerRepository.findById(2);
        assertFalse(deletedCustomer.isPresent());
    }

    @Test
    public void updateAddressTest(){
        Optional<Customer> customer = customerRepository.findById(2);
        assertTrue(customer.isPresent());
        Customer customerToUpdate = new Customer("Updated_Customer_Name", 3);
        customerService.update(2, customerToUpdate);
        Customer updatedCustomer = customerRepository.findById(2).get();
        assertEquals("Updated_Customer_Name", updatedCustomer.getName());
    }
}
