package ua.foxminded.warehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.warehouse.models.Customer;
import ua.foxminded.warehouse.models.Invoice;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    public List<Invoice> findInvoicesByCustomerId(int customerId);
    public Invoice findByPrice(BigDecimal price);
    public Invoice findByDate(Date date);

    /*Find all invoices which has customer_rate of customer more then X and price of item more than Y*/
    @Query(value = "SELECT i.id, i.date, i.customer_id, i.item_id, i.price, i.item_count, i.stage, \n" +
            "    avg(c.customer_rate) from invoice i join customer c on i.customer_id = c.id \n" +
            "    join item i2 on i2.id = i.item_id\n" +
            "    WHERE i2.price > :price\n" +
            "    GROUP BY i.id having avg(c.customer_rate) > :customerRate", nativeQuery = true)
    public List<Invoice> findInvoicesByCustomerRateAndPrice(
            @Param("customerRate") int customerRate, @Param("price") BigDecimal price);
}
