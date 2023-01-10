package ua.foxminded.warehouse.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.warehouse.models.Address;
import ua.foxminded.warehouse.repositories.AddressRepository;
import ua.foxminded.warehouse.restcontrollers.Path;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class AddressRestControllerTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${sql.script.create.address}")
    private String sqlAddAddress;

    @Value("${sql.script.delete.address}")
    private String sqlDeleteAddresses;

    public  static final MediaType APPLICATION_JSON_UTF8 =MediaType.APPLICATION_JSON;

    @BeforeEach
    public void setupDataBase() {
        jdbcTemplate.execute(sqlAddAddress);
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbcTemplate.execute(sqlDeleteAddresses);
    }

    @Test
    public void getAddressesHttpRequest_ShouldReturnStatusIsOkAndJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Path.ADDRESS+Path.GET_ALL_ADDRESS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getAllAddressHttpRequest_ShouldReturnErrorNotFound() throws Exception {
        jdbcTemplate.execute(sqlDeleteAddresses);
        assertTrue(addressRepository.findAll().isEmpty());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.ADDRESS+Path.GET_ALL_ADDRESS))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    public void createAddressHttpRequestTest_ShouldReturnStatusIsOkAndJson() throws Exception {
        Address address = new Address(
                22222, "CountryTest2", "CityTest2", "StreetTest2", 222);
        mockMvc.perform(MockMvcRequestBuilders.post(Path.ADDRESS+Path.CREATE_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                    .andExpect(status().isOk());
    }

    @Test
    public void createAddressHttpRequestTest_ShouldReturnErrorBadRequest() throws Exception {
        Address address = new Address(
                22222, "", "CityTest2", "StreetTest2", 222);
        mockMvc.perform(MockMvcRequestBuilders.post(Path.ADDRESS + Path.CREATE_ADDRESS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                    .andExpect(status().is4xxClientError())
                    .andExpect(status().isBadRequest());
    }

        @Test
    public void findByIdAddressTest_ShouldReturnJson() throws Exception {
        assertTrue(addressRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.ADDRESS+Path.GET_ADDRESS_BY_ID, 2))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                    .andExpect(jsonPath("$.postcode", is(10000)));
    }

    @Test
    public void findByIdAddressTest_ShouldReturnErrorNotFound() throws Exception {
        assertFalse(addressRepository.findById(10).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.ADDRESS+Path.GET_ADDRESS_BY_ID, 10))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateAddressByRequestTest_ShouldReturnStatusIsOk() throws Exception {
        assertTrue(addressRepository.findById(2).isPresent());
        Address address = new Address(
                22222, "UpdatedCountry", "CityTest2", "StreetTest2", 222);
        mockMvc.perform(MockMvcRequestBuilders.put(Path.ADDRESS+Path.UPDATE_ADDRESS, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                    .andExpect(status().isOk());
    }

    @Test
    public void updateAddressHttpRequestTest_ShouldReturnErrorBadRequest() throws Exception {
        assertTrue(addressRepository.findById(2).isPresent());
        Address address = new Address(
                22222, "", "", "StreetTest2", 222);
        mockMvc.perform(MockMvcRequestBuilders.put(Path.ADDRESS + Path.UPDATE_ADDRESS, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(address)))
                    .andExpect(status().is4xxClientError())
                    .andExpect(status().isBadRequest());
    }

    @Test
    public void updateAddressByRequestTest_ShouldReturnErrorNotFound() throws Exception {
        assertFalse(addressRepository.findById(10).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.put(Path.ADDRESS+Path.UPDATE_ADDRESS, 10))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteAddressHttpRequestTest_ShouldReturnStatusIsOk() throws Exception {
        assertTrue(addressRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(Path.ADDRESS+Path.DELETE_ADDRESS_BY_ID, 2))
                    .andExpect(status().isOk());
    }

    @Test
    public void deleteAddressHttpRequestErrorNotFound() throws Exception {
        assertFalse(addressRepository.findById(20).isPresent());
            mockMvc.perform(MockMvcRequestBuilders.delete(Path.ADDRESS+Path.DELETE_ADDRESS_BY_ID, 20))
                    .andExpect(status().is4xxClientError())
                    .andExpect(status().isNotFound());
    }
}
