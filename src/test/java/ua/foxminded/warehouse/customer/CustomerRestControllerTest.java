package ua.foxminded.warehouse.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.warehouse.dto.CustomerDTO;
import ua.foxminded.warehouse.repositories.CustomerRepository;
import ua.foxminded.warehouse.restcontrollers.Path;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class CustomerRestControllerTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MockMvc mockMvc;

    public  static final MediaType APPLICATION_JSON_UTF8 =MediaType.APPLICATION_JSON;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void getCustomerHttpRequest_ShouldReturnStatusIsOkAndJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Path.CUSTOMER+Path.GET_ALL_CUSTOMER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    public void getAllCustomerHttpRequest_ShouldReturnErrorNotFound() throws Exception {
        assertTrue(customerRepository.findAll().isEmpty());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.CUSTOMER+Path.GET_ALL_CUSTOMER))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void createCustomerHttpRequestTest_ShouldReturnStatusIsOkAndJson() throws Exception {
        CustomerDTO customer = new CustomerDTO("Saved_Customer_Name", 3, 2);
        mockMvc.perform(MockMvcRequestBuilders.post(Path.CUSTOMER+Path.CREATE_CUSTOMER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void createCustomerHttpRequestTest_ShouldReturnErrorBadRequest() throws Exception {
        CustomerDTO customer = new CustomerDTO("", 3, 3);
        mockMvc.perform(MockMvcRequestBuilders.post(Path.CUSTOMER+Path.CREATE_CUSTOMER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findByIdCustomerTest_ShouldReturnJson() throws Exception {
        assertTrue(customerRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.CUSTOMER+Path.GET_CUSTOMER_BY_ID, 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Center")));
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findByIdCustomerTest_ShouldReturnErrorNotFound() throws Exception {
        assertFalse(customerRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.CUSTOMER+Path.GET_CUSTOMER_BY_ID, 100))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void updateCustomerByRequestTest_ShouldReturnStatusIsOk() throws Exception {
        assertTrue(customerRepository.findById(2).isPresent());
        CustomerDTO customer = new CustomerDTO("Updated_Customer_Name", 3, 3);
        mockMvc.perform(MockMvcRequestBuilders.put(Path.CUSTOMER+Path.UPDATE_CUSTOMER, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void updateCustomerHttpRequestTest_ShouldReturnErrorBadRequest() throws Exception {
        assertTrue(customerRepository.findById(2).isPresent());
        CustomerDTO customer = new CustomerDTO("", 3, 3);
        mockMvc.perform(MockMvcRequestBuilders.put(Path.CUSTOMER+Path.UPDATE_CUSTOMER, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void updateCustomerByRequestTest_ShouldReturnErrorNotFound() throws Exception {
        assertFalse(customerRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.put(Path.CUSTOMER+Path.UPDATE_CUSTOMER, 100))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void deleteCustomerHttpRequestTest_ShouldReturnStatusIsOk() throws Exception {
        assertTrue(customerRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(Path.CUSTOMER+Path.DELETE_CUSTOMER_BY_ID, 2))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void deleteCustomerHttpRequestErrorNotFound() throws Exception {
        assertFalse(customerRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(Path.CUSTOMER+Path.DELETE_CUSTOMER_BY_ID, 100))
                .andExpect(status().is4xxClientError());
    }
}
