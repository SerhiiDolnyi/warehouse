package ua.foxminded.warehouse.offer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.warehouse.models.*;
import ua.foxminded.warehouse.repositories.OfferRepository;
import ua.foxminded.warehouse.services.OfferService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class OfferDBIntegrationTest {

    @Autowired
    private OfferService offerService;

    @Autowired
    private OfferRepository offerRepository;

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findAllOffers_ShouldReturnSizeOfList() {
        List<Offer> offers = offerService.findAll();
        assertEquals(3, offers.size());
    }

    @ParameterizedTest
    @CsvSource({"0, 1", "1, 2", "2, 3"})
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findAllOffers_ShouldReturnItemCountOfOffer(int index, int itemCount) {
        List<Offer> offers = offerService.findAll();
        Offer offer = offers.get(index);
        assertEquals(itemCount, offer.getItemCount());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findOfferByIdTest_ShouldReturnItemCountOfFoundOffer() {
        Offer foundOffer = offerService.findById(2);
        assertEquals(2, foundOffer.getItemCount());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void saveOfferTest_ShouldReturnDateOfSavedInvoice() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2025-01-01";
        Date date = formatter.parse(dateInString);
        Offer offer = new Offer(date, BigDecimal.valueOf(30.00), 1, "Registered");

        offerService.save(offer);
        Offer savedOffer = offerRepository.findByDate(date);
        assertEquals(date, savedOffer.getDate());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void deleteOfferTest_ShouldReturnFalse(){
        Optional<Offer> deletedOffer = offerRepository.findById(2);
        assertTrue(deletedOffer.isPresent());
        offerService.delete(2);
        deletedOffer = offerRepository.findById(2);
        assertFalse(deletedOffer.isPresent());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void updateInvoiceTest_ShouldReturnUpdatedDate() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2025-01-01";
        Date date = formatter.parse(dateInString);
        Offer offerToUpdate = new Offer(date, BigDecimal.valueOf(200.00), 1, "Registered");

        offerService.update(2, offerToUpdate);
        Offer savedOffer = offerRepository.findById(2).get();
        assertEquals(date, savedOffer.getDate());
    }
}
