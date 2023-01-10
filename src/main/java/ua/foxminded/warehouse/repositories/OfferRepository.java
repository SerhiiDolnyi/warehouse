package ua.foxminded.warehouse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.foxminded.warehouse.models.Offer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {
    public List<Offer> findOfferBySupplierId(int supplierId);
    public Offer findByDate(Date date);

    /*Find all offers which has price of item more than X and supplier from city Y*/
    @Query(value = "SELECT o.id, o.date, o.supplier_id, o.item_id, o.price, o.item_count, o.stage FROM offer o\n" +
            "    join supplier s on o.supplier_id = s.id join address a on a.id = s.address_id\n" +
            "    WHERE o.price > :price and a.city = :city", nativeQuery = true)
    public List<Offer> findOffersByPriceAndSupplierCity(@Param("price") BigDecimal price, @Param("city") String city);
}
