package ua.foxminded.warehouse.address;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import ua.foxminded.warehouse.models.Address;
import ua.foxminded.warehouse.repositories.AddressRepository;
import ua.foxminded.warehouse.services.AddressService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    @DisplayName("AddressService Find Address by ID Test")
    @Test
    public void findByIdTest_ShouldReturnAddress(){
        Address address = new Address(
                11111, "CountryTest", "CityTest", "StreetTest", 111);
        when(addressRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(address));
        assertEquals(address, addressService.findById(Mockito.anyInt()));
        verify(addressRepository).findById(Mockito.anyInt());
    }

    @DisplayName("AddressService findAll Test")
    @Test
    public void findAllTest_ShouldReturnAllAddresses(){
        Address address1 = new Address(
                11111, "CountryTest1", "CityTest1", "StreetTest1", 111);
        Address address2 = new Address(
                22222, "CountryTest2", "CityTest2", "StreetTest2", 222);
        List<Address> addresses = new ArrayList<>();
        addresses.add(address1);
        addresses.add(address2);
        when(addressRepository.findAll()).thenReturn(addresses);
        assertEquals(addresses, addressService.findAll());
        verify(addressRepository).findAll();
    }

    @DisplayName("AddressService save Test Should verify save method of AddressRepository")
    @Test
    public void saveAddressTest_ShouldVerifySaveMethodOfAddressRepository() {
        Address address = new Address();
        addressService.save(address);
        verify(addressRepository).save(address);
    }

    @DisplayName("AddressService Update Test Should verify save method of AddressRepository")
    @Test
    public void updateAddressTest_ShouldVerifySaveMethodOfAddressRepository() {
        Address address = new Address();
        addressService.update(1, address);
        verify(addressRepository).save(address);
    }

    @DisplayName("AddressService delete Test Should Verify delete method Of AddressRepository")
    @Test
    public void deleteAddresServiceTest_ShouldVerifyDeleteMethodOfAddressRepository() {
        addressService.delete(1);
        verify(addressRepository).deleteById(1);
    }
}
