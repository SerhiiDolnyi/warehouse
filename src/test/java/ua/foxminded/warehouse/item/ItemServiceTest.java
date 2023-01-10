package ua.foxminded.warehouse.item;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.foxminded.warehouse.models.Item;
import ua.foxminded.warehouse.repositories.ItemRepository;
import ua.foxminded.warehouse.services.ItemService;
import ua.foxminded.warehouse.util.exception.item.ItemNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemServiceTest {
    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Test
    public void findByIdTest_ShouldReturnItem(){
        Item item = new Item(
                "testName", "testCategory", "testDescription", BigDecimal.valueOf(100), 1);
        when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        assertEquals(item, itemService.findById(Mockito.anyInt()));
        verify(itemRepository).findById(Mockito.anyInt());
    }

    @Test
    public void findByIdTest_ShouldReturnItemNotFoundException() {
       doThrow(new ItemNotFoundException()).when(itemRepository).findById(1);
       assertThrows(ItemNotFoundException.class, () -> itemService.findById(1));
       verify(itemRepository).findById(1);
    }

    @Test
    public void findAllTest_ShouldReturnListOfItems(){
        Item item1 = new Item(
                "testName1", "testCategory1", "testDescription1", BigDecimal.valueOf(100), 1);
        Item item2 = new Item(
                "testName2", "testCategory2", "testDescription2", BigDecimal.valueOf(200), 2);
        List<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        when(itemRepository.findAll()).thenReturn(items);
        assertEquals(items, itemService.findAll());
        verify(itemRepository).findAll();
    }

    @Test
    public void saveItemServiceTest_ShouldVerifySaveMethodOfItemRepository() {
        Item item = new Item();
        itemService.save(item);
        verify(itemRepository).save(item);
    }

    @Test
    public void updateItemServiceTest_ShouldVerifyUpdateMethodOfItemRepocitory() {
        Item item = new Item();
        itemService.update(1, item);
        verify(itemRepository).save(item);
    }

    @Test
    public void deleteItemServiceTest_ShouldVerifyDeleteMethodOfItemRepository() {
        itemService.delete(1);
        verify(itemRepository).deleteById(1);
    }
}
