package ua.foxminded.warehouse.invoice;

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
import ua.foxminded.warehouse.dto.InvoiceDTO;
import ua.foxminded.warehouse.repositories.CustomerRepository;
import ua.foxminded.warehouse.repositories.InvoiceRepository;
import ua.foxminded.warehouse.repositories.ItemRepository;
import ua.foxminded.warehouse.restcontrollers.Path;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public class InvoiceRestControllerTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public  static final MediaType APPLICATION_JSON_UTF8 =MediaType.APPLICATION_JSON;

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void getAllInvoicesHttpRequest_ShouldReturnStatusIsOkAndJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Path.INVOICE+Path.GET_ALL_INVOICE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    public void getAllInvoicesHttpRequest_ShouldReturnErrorNotFound() throws Exception {
        assertTrue(invoiceRepository.findAll().isEmpty());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.INVOICE+Path.GET_ALL_INVOICE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void createInvoiceHttpRequestTest_ShouldReturnStatusIsOkAndJson() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2025-01-01";
        Date date = formatter.parse(dateInString);
        InvoiceDTO invoiceDTO = new InvoiceDTO(date, 2, 2, BigDecimal.valueOf(200.00), 1, "Registered");

        mockMvc.perform(MockMvcRequestBuilders.post(Path.INVOICE+Path.CREATE_INVOICE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoiceDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    public void createInvoiceHttpRequestTest_ShouldReturnErrorBadRequest() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2020-01-01";
        Date date = formatter.parse(dateInString);
        InvoiceDTO invoiceDTO = new InvoiceDTO(date, 2, 2, BigDecimal.valueOf(200.00), 1, "");

        mockMvc.perform(MockMvcRequestBuilders.post(Path.INVOICE+Path.CREATE_INVOICE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoiceDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findByIdInvoiceTest_ShouldReturnJson() throws Exception {
        assertTrue(invoiceRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.INVOICE+Path.GET_INVOICE_BY_ID, 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.customerId", is(2)))
                .andExpect(jsonPath("$.itemId", is(2)))
                .andExpect(jsonPath("$.itemCount", is(1)))
                .andExpect(jsonPath("$.stage", is("Registered")));
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findByIdInvoiceTest_ShouldReturnErrorNotFound() throws Exception {
        assertFalse(invoiceRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.INVOICE+Path.GET_INVOICE_BY_ID, 100))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void updateInvoiceByRequestTest_ShouldReturnStatusIsOk() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2025-01-01";
        Date date = formatter.parse(dateInString);
        InvoiceDTO invoiceToUpdate = new InvoiceDTO(date, 2, 2, BigDecimal.valueOf(200.00), 1, "Registered");

        assertTrue(invoiceRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.put(Path.INVOICE+Path.UPDATE_INVOICE, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoiceToUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void updateInvoiceHttpRequestTest_ShouldReturnErrorBadRequest() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2019-01-01";
        Date date = formatter.parse(dateInString);
        InvoiceDTO invoiceToUpdate = new InvoiceDTO(date, 2, 2, BigDecimal.valueOf(200.00), 1, "");

        assertTrue(invoiceRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.put(Path.INVOICE+Path.UPDATE_INVOICE, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invoiceToUpdate)))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void updateInvoiceByRequestTest_ShouldReturnErrorNotFound() throws Exception {
        assertFalse(invoiceRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.put(Path.INVOICE+Path.UPDATE_INVOICE, 100))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void deleteInvoiceHttpRequestTest_ShouldReturnStatusIsOk() throws Exception {
        assertTrue(invoiceRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(Path.INVOICE+Path.DELETE_INVOICE_BY_ID, 2))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void deleteSInvoiceHttpRequestErrorNotFound() throws Exception {
        assertFalse(invoiceRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(Path.INVOICE+Path.DELETE_INVOICE_BY_ID, 100))
                .andExpect(status().is4xxClientError());
    }
}
