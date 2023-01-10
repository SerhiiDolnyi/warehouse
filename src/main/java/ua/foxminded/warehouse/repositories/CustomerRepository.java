package ua.foxminded.warehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.foxminded.warehouse.models.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findCustomerByName(String name);

    /*Query to select first three customers, whose invoices are the most expensive*/
    @Query(value = "SELECT c.id, c.name, c.address_id, c.customer_rate FROM customer c " +
            "join invoice i on c.id = i.id order by price desc limit 3", nativeQuery = true)
    List<Customer> findFirst3CustomerByTheMostExpensiveInvoice ();
}
