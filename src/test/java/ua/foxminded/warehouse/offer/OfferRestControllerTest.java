package ua.foxminded.warehouse.offer;

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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.warehouse.dto.InvoiceDTO;
import ua.foxminded.warehouse.dto.OfferDTO;
import ua.foxminded.warehouse.models.Invoice;
import ua.foxminded.warehouse.models.Offer;
import ua.foxminded.warehouse.repositories.*;
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
public class OfferRestControllerTest {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public  static final MediaType APPLICATION_JSON_UTF8 =MediaType.APPLICATION_JSON;

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void getAllOffersHttpRequest_ShouldReturnStatusIsOkAndJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Path.OFFER+Path.GET_ALL_OFFER))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    public void getAllOffersHttpRequest_ShouldReturnErrorNotFound() throws Exception {
        assertTrue(offerRepository.findAll().isEmpty());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.OFFER+Path.GET_ALL_OFFER))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void createOfferHttpRequestTest_ShouldReturnStatusIsOkAndJson() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2030-01-01";
        Date date = formatter.parse(dateInString);
        OfferDTO offerDTO = new OfferDTO(date, 2, 2, BigDecimal.valueOf(200.00), 1, "Registered");

        mockMvc.perform(MockMvcRequestBuilders.post(Path.OFFER+Path.CREATE_OFFER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void createOfferHttpRequestTest_ShouldReturnErrorBadRequest() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2020-01-01";
        Date date = formatter.parse(dateInString);
        OfferDTO offerDTO = new OfferDTO(date, 2, 2, BigDecimal.valueOf(200.00), 1, "");

        mockMvc.perform(MockMvcRequestBuilders.post(Path.OFFER+Path.CREATE_OFFER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findByIdOfferTest_ShouldReturnJson() throws Exception {
        assertTrue(offerRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.OFFER+Path.GET_OFFER_BY_ID, 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.supplierId", is(2)))
                .andExpect(jsonPath("$.itemId", is(2)))
                .andExpect(jsonPath("$.itemCount", is(2)))
                .andExpect(jsonPath("$.stage", is("Fulfilled")));
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findByIdOfferTest_ShouldReturnErrorNotFound() throws Exception {
        assertFalse(offerRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.OFFER+Path.GET_OFFER_BY_ID, 100))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void updateInvoiceByRequestTest_ShouldReturnStatusIsOk() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2030-01-01";
        Date date = formatter.parse(dateInString);
        OfferDTO offerDTO = new OfferDTO(date, 2, 2, BigDecimal.valueOf(200.00), 1, "Registered");

        assertTrue(offerRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.put(Path.OFFER+Path.UPDATE_OFFER, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void updateInvoiceHttpRequestTest_ShouldReturnErrorBadRequest() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2020-01-01";
        Date date = formatter.parse(dateInString);
        OfferDTO offerDTO = new OfferDTO(date, 2, 2, BigDecimal.valueOf(200.00), 1, "");

        assertTrue(offerRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.put(Path.OFFER+Path.UPDATE_OFFER, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(offerDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    public void updateOfferByRequestTest_ShouldReturnErrorNotFound() throws Exception {
        assertFalse(offerRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.put(Path.OFFER+Path.UPDATE_OFFER, 100))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void deleteOfferHttpRequestTest_ShouldReturnStatusIsOk() throws Exception {
        assertTrue(offerRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(Path.OFFER+Path.DELETE_OFFER_BY_ID, 2))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void deleteOfferHttpRequestErrorNotFound() throws Exception {
        assertFalse(offerRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(Path.OFFER+Path.DELETE_OFFER_BY_ID, 100))
                .andExpect(status().is4xxClientError());
    }
}
