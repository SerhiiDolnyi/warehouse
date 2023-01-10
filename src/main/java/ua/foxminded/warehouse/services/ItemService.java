package ua.foxminded.warehouse.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.warehouse.models.Address;
import ua.foxminded.warehouse.models.Customer;
import ua.foxminded.warehouse.models.Item;
import ua.foxminded.warehouse.repositories.ItemRepository;
import ua.foxminded.warehouse.util.exception.customer.CustomerNotFoundException;
import ua.foxminded.warehouse.util.exception.item.ItemNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ItemService {
    ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAll(){
        return itemRepository.findAll();
    }

    public Item findById(int itemId){
        Optional<Item> foundItem = itemRepository.findById(itemId);
        return foundItem.orElseThrow(ItemNotFoundException::new);
    }

    @Transactional
    public void save (Item item){
        itemRepository.save(item);
    }

    @Transactional
    public void update (int itemId, Item updatedItem){
        updatedItem.setId(itemId);
        itemRepository.save(updatedItem);
    }

    @Transactional
    public void delete (int itemId){
        itemRepository.deleteById(itemId);
    }

}
