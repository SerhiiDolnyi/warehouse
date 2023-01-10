package ua.foxminded.warehouse.manager;

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
import ua.foxminded.warehouse.restcontrollers.Path;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class ManagerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    public  static final MediaType APPLICATION_JSON_UTF8 =MediaType.APPLICATION_JSON;

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void showItemByAmountLessThanTest_ShouldReturnStatusIsOkAndJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Path.MANAGER+Path.ITEM_BY_AMOUNT_LESS_THAN).param("itemAmount", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findInvoicesByCustomerIdTest_ShouldReturnStatusIsOkAndJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Path.MANAGER+Path.INVOICES_BY_CUSTOMER_ID).param("customerId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    @Sql(scripts = "/clean-up-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findOfferBySupplierIdTest_ShouldReturnStatusIsOkAndJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Path.MANAGER+Path.OFFER_BY_SUPPLIER_ID).param("supplierId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findFirst3CustomerByTheMostExpensiveInvoiceTest_ShouldReturnStatusIsOkAndJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Path.MANAGER+Path.FIRST_3_CUSTOMERS_BY_MOST_EXPENSIVE_INVOICE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findInvoicesByCustomerRateAndPriceTest_ShouldReturnStatusIsOkAndJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Path.MANAGER+Path.INVOICES_BY_CUSTOMERRATE_AND_PRICE)
                    .param("customerRate", "2").param("price", "200.00"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findOffersByPriceAndSupplierCityTest_ShouldReturnStatusIsOkAndJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Path.MANAGER+Path.OFFERS_BY_PRICE_AND_SUPPLIERCITY)
                .param("price", "10.00").param("city", "Kyiv"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

}
