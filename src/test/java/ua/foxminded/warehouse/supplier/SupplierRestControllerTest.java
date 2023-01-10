package ua.foxminded.warehouse.supplier;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.warehouse.dto.SupplierDTO;
import ua.foxminded.warehouse.models.Supplier;
import ua.foxminded.warehouse.repositories.SupplierRepository;
import ua.foxminded.warehouse.restcontrollers.Path;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class SupplierRestControllerTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${sql.script.create.address}")
    private String sqlAddAddress;

    @Value("${sql.script.create.address3}")
    private String sqlAddAddress3;

    @Value("${sql.script.create.supplier2}")
    private String sqlAddSupplier2;

    @Value("${sql.script.create.supplier3}")
    private String sqlAddSupplier3;

    @Value("${sql.script.delete.address}")
    private String sqlDeleteAddresses;

    @Value("${sql.script.delete.supplier}")
    private String sqlDeleteSupplier;

    public  static final MediaType APPLICATION_JSON_UTF8 =MediaType.APPLICATION_JSON;

    @BeforeEach
    public void setupDatabase() {
        jdbcTemplate.execute(sqlAddAddress);
        jdbcTemplate.execute(sqlAddSupplier2);
    }

    @AfterEach
    public void setupAfterTransaction(){
        jdbcTemplate.execute(sqlDeleteSupplier);
        jdbcTemplate.execute(sqlDeleteAddresses);
    }
    @Test
    public void getSupplierHttpRequest_ShouldReturnStatusIsOkAndJson() throws Exception {
        jdbcTemplate.execute(sqlAddAddress3);
        jdbcTemplate.execute(sqlAddSupplier3);
        mockMvc.perform(MockMvcRequestBuilders.get(Path.SUPPLIER+Path.GET_ALL_SUPPLIER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getAllSupplierHttpRequest_ShouldReturnErrorNotFound() throws Exception {
        jdbcTemplate.execute(sqlDeleteSupplier);
        assertTrue(supplierRepository.findAll().isEmpty());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.SUPPLIER+Path.GET_ALL_SUPPLIER))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createSupplierHttpRequestTest_ShouldReturnStatusIsOkAndJson() throws Exception {
        SupplierDTO supplierDTO = new SupplierDTO("Saved_Supplier_Name", 3, 2);
        mockMvc.perform(MockMvcRequestBuilders.post(Path.SUPPLIER+Path.CREATE_SUPPLIER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supplierDTO)))
                .andExpect(status().isOk());
        Supplier verifySupplier = supplierRepository.findSupplierByName("Saved_Supplier_Name").get();
        assertEquals("Saved_Supplier_Name", verifySupplier.getName());
    }

    @Test
    public void createSupplierHttpRequestTest_ShouldReturnErrorBadRequest() throws Exception {
        jdbcTemplate.execute(sqlAddAddress3);
        SupplierDTO supplierDTO = new SupplierDTO("", 3, 2);
        mockMvc.perform(MockMvcRequestBuilders.post(Path.SUPPLIER+Path.CREATE_SUPPLIER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supplierDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findByIdSupplierTest_ShouldReturnJson() throws Exception {
        assertTrue(supplierRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.SUPPLIER+Path.GET_SUPPLIER_BY_ID, 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("test_SupplierName2")));
    }

    @Test
    public void findByIdSupplierTest_ShouldReturnErrorNotFound() throws Exception {
        assertFalse(supplierRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.SUPPLIER+Path.GET_SUPPLIER_BY_ID, 100))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateSupplierByRequestTest_ShouldReturnStatusIsOk() throws Exception {
        jdbcTemplate.execute(sqlAddAddress3);
        assertTrue(supplierRepository.findById(2).isPresent());
        SupplierDTO supplierDTO = new SupplierDTO("Updated_Supplier_Name", 3, 3);
        mockMvc.perform(MockMvcRequestBuilders.put(Path.SUPPLIER+Path.UPDATE_SUPPLIER, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supplierDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateSupplierHttpRequestTest_ShouldReturnErrorBadRequest() throws Exception {
        assertTrue(supplierRepository.findById(2).isPresent());
        SupplierDTO supplierDTO = new SupplierDTO("", 3, 3);
        mockMvc.perform(MockMvcRequestBuilders.put(Path.SUPPLIER+Path.UPDATE_SUPPLIER, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(supplierDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateSupplierByRequestTest_ShouldReturnErrorNotFound() throws Exception {
        assertFalse(supplierRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.put(Path.SUPPLIER+Path.UPDATE_SUPPLIER, 100))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deleteSupplierHttpRequestTest_ShouldReturnStatusIsOk() throws Exception {
        assertTrue(supplierRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(Path.SUPPLIER+Path.DELETE_SUPPLIER_BY_ID, 2))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteSupplierHttpRequestErrorNotFound() throws Exception {
        assertFalse(supplierRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(Path.SUPPLIER+Path.DELETE_SUPPLIER_BY_ID, 100))
                .andExpect(status().is4xxClientError());
    }
}
