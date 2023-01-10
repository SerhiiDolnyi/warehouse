package ua.foxminded.warehouse.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.warehouse.models.Address;
import ua.foxminded.warehouse.models.Item;
import ua.foxminded.warehouse.repositories.ItemRepository;
import ua.foxminded.warehouse.restcontrollers.Path;
import ua.foxminded.warehouse.services.ItemService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class ItemRestControllerTest {
    private static MockHttpServletRequest request;

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
    public void getAllItemHttpRequest_ShouldReturnStatusIsOkAndJson() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(Path.ITEM+Path.GET_ALL_ITEM))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(7)));
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    public void getAllItemHttpRequest_ShouldReturnErrorNotFound() throws Exception {
        assertTrue(itemRepository.findAll().isEmpty());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.ITEM+Path.GET_ALL_ITEM))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void createItemHttpRequestTest_ShouldReturnStatusIsOkAndJson() throws Exception {
        Item item = new Item(
                "SaveMethodTest Name", "testCategory", "testDescription", BigDecimal.valueOf(100), 1);
        mockMvc.perform(MockMvcRequestBuilders.post(Path.ITEM+Path.CREATE_ITEM)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                    .andExpect(status().isOk());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    public void createItemHttpRequestTest_ShouldReturnErrorBadRequest() throws Exception {
        Item item = new Item(
                "", "", "testDescription", BigDecimal.valueOf(100), 1);
        mockMvc.perform(MockMvcRequestBuilders.post(Path.ITEM+Path.CREATE_ITEM)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findByIdItemTest_ShouldReturnJson() throws Exception {
        assertTrue(itemRepository.findById(2).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.ITEM+Path.GET_ITEM_BY_ID, 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("SQL from scratch")))
                .andExpect(jsonPath("$.category", is("book")));
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findByIdItemTest_ShouldReturnErrorNotFound() throws Exception {
        assertFalse(itemRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.get(Path.ITEM+Path.GET_ITEM_BY_ID, 100))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void updateItemByRequestTest_ShouldReturnStatusIsOk() throws Exception {
        assertTrue(itemRepository.findById(2).isPresent());
        Item updatedItem  = new Item("updatedName", "updatedCategory",
                    "updatedDescription", BigDecimal.valueOf(200), 2);
        mockMvc.perform(MockMvcRequestBuilders.put(Path.ITEM+Path.UPDATE_ITEM, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedItem)))
                    .andExpect(status().isOk());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void updateItemHttpRequestTest_ShouldReturnErrorBadRequest() throws Exception {
        assertTrue(itemRepository.findById(2).isPresent());
        Item updatedItem  = new Item("", "",
                "updatedDescription", BigDecimal.valueOf(200), 2);
        mockMvc.perform(MockMvcRequestBuilders.put(Path.ITEM+Path.UPDATE_ITEM, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedItem)))
                    .andExpect(status().is4xxClientError())
                    .andExpect(status().isBadRequest());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void updateItemByRequestTest_ShouldReturnErrorNotFound() throws Exception {
        assertFalse(itemRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.put(Path.ITEM+Path.UPDATE_ITEM, 100))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void deleteItemHttpRequestTest_ShouldReturnStatusIsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(Path.ITEM+Path.DELETE_ITEM_BY_ID, 2))
                .andExpect(status().isOk());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void deleteItemHttpRequestErrorNotFound() throws Exception {
        assertFalse(itemRepository.findById(100).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(Path.DELETE_ITEM_BY_ID,100))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());
    }
}
