package ua.foxminded.warehouse.address;

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
import ua.foxminded.warehouse.models.Address;
import ua.foxminded.warehouse.repositories.AddressRepository;
import ua.foxminded.warehouse.services.AddressService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class AddressDBIntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressRepository addressRepository;

    @Value("${sql.script.create.address}")
    private String sqlAddAddress;

    @Value("${sql.script.create.address3}")
    private String sqlAddAddress2;

    @Value("${sql.script.delete.address}")
    private String sqlDeleteAddresses;

    @BeforeEach
    public void setupDatabase() {
        jdbcTemplate.execute(sqlAddAddress);
    }

    @AfterEach
    public void setupAfterTransaction(){
        jdbcTemplate.execute(sqlDeleteAddresses);
    }

    @ParameterizedTest
    @CsvSource({"0,200", "1, 300"})
    public void findAllAddressByIdTest_ShouldReturnFieldOfOfficeNumber(int index, int office){
        jdbcTemplate.execute(sqlAddAddress2);
        List<Address> addresses = addressService.findAll();
        Address address = addresses.get(index);
        assertEquals(office, address.getOffice());
    }

    @Test
    public void findAllAddressTest_ShouldReturnSizeOfList(){
        jdbcTemplate.execute(sqlAddAddress2);
        List<Address> addresses = addressService.findAll();
        assertEquals(2, addresses.size());
    }

    @Test
    public void findAddressByIdTest_ShouldReturnFieldOfPostcode(){
        Address address = addressService.findById(2);
        assertEquals(10000, address.getPostcode());
    }


    @Test
    public void saveAddressTestShouldReturnCountryOfSavedAddress() {
        Address address = new Address(11111, "Country", "Test", "Test", 111);
        addressService.save(address);
        Address savedAddress = addressRepository.findByCountry("Country");
        assertEquals("Country", savedAddress.getCountry());
    }

    @Test
    public void deleteAddressTest(){
        Optional<Address> deletedAddress = addressRepository.findById(2);
        assertTrue(deletedAddress.isPresent());
        addressService.delete(2);
        deletedAddress = addressRepository.findById(100);
        assertFalse(deletedAddress.isPresent());
    }

    @Test
    public void updateAddressTest(){
        Optional<Address> address = addressRepository.findById(2);
        assertTrue(address.isPresent());
        Address updatedAddress = new Address(11111, "Test Country", "Test", "Test", 111);
        addressService.update(2,updatedAddress);
        updatedAddress = addressRepository.findById(2).get();
        assertEquals("Test Country", updatedAddress.getCountry());
    }
}
