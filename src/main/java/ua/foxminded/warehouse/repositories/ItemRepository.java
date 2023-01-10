package ua.foxminded.warehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.foxminded.warehouse.models.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Item findByName(String itemName);
    List<Item> findItemByAmountLessThan(int count);
}
