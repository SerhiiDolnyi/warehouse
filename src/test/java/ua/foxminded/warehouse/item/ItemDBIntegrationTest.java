package ua.foxminded.warehouse.item;

import lombok.experimental.StandardException;
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
import ua.foxminded.warehouse.models.Item;
import ua.foxminded.warehouse.repositories.ItemRepository;
import ua.foxminded.warehouse.services.ItemService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class ItemDBIntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Value("${sql.script.create.item2}")
    private String sqlAddItem;

    @Value("${sql.script.create.item3}")
    private String sqlAddItem2;

    @Value("${sql.script.delete.item}")
    private String sqlDeleteItems;

    @BeforeEach
    public void setupDataBase() {
        jdbcTemplate.execute(sqlAddItem);
    }

    @AfterEach
    public void setupAfterTransaction(){
        jdbcTemplate.execute(sqlDeleteItems);
    }

    @Test
    public void findItemByIdTest_ShouldReturnItem(){
        Item item = new Item(2, "testName", "testCategory", "testDescription", BigDecimal.valueOf(100), 1);
        assertEquals(item, itemService.findById(2));
    }

    @Test
    public void findAllItemsTest_ShouldReturnSizeOfList(){
        jdbcTemplate.execute(sqlAddItem2);
        List<Item> items = itemService.findAll();
        assertEquals(2, items.size());
    }

    @ParameterizedTest
    @CsvSource({"0, 2", "1, 3"})
    public void findAllItemsTest_ShouldReturnAmountOfItem(int index,  int amount){
        jdbcTemplate.execute(sqlAddItem2);
        List<Item> items = itemService.findAll();
        Item item = items.get(index);
        assertEquals(amount, item.getAmount());
    }

    @Test
    public void saveItemTest_ShouldReturnNameOfSavedItem(){
        Item item = new Item("savedName", "savedCategory", "savedDescription", BigDecimal.valueOf(100), 1);
        itemRepository.save(item);
        Item savedItem = itemRepository.findByName("savedName");
        assertEquals("savedName", savedItem.getName());
    }

    @Test
    public void deleteItemTest_ShouldReturnFalseIfItemExist(){
        Optional<Item> deletedItem = itemRepository.findById(2);
        assertTrue(deletedItem.isPresent());
        itemService.delete(2);
        deletedItem = itemRepository.findById(2);
        assertFalse(deletedItem.isPresent());
    }

    @Test
    public void updateItemTest_ShouldReturnUpdatedItem(){
        Optional<Item> item = itemRepository.findById(2);
        assertTrue(item.isPresent());
        Item updatedItem  = new Item(
                "updatedName", "updatedCategory", "updatedDescription", BigDecimal.valueOf(200), 2);
        itemService.update(2, updatedItem);
        Item savedItem = itemRepository.findById(2).get();
        assertEquals(updatedItem, savedItem);
    }
}
