package ua.foxminded.warehouse.offer;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.warehouse.models.Invoice;
import ua.foxminded.warehouse.models.Offer;
import ua.foxminded.warehouse.repositories.OfferRepository;
import ua.foxminded.warehouse.services.OfferService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OfferServiceTest {
    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private OfferService offerService;

    @Test
    public void findByIdTest_ShouldReturnOffer() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2025-01-01";
        Date date = formatter.parse(dateInString);
        Offer offer = new Offer(date);
        when(offerRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(offer));
        assertEquals(offer, offerRepository.findById(1).get());
        verify(offerRepository).findById(1);
    }

    @Test
    public void findAllTest_ShouldReturnAllOffers() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString1 = "2036-01-01";
        Date date1 = formatter.parse(dateInString1);
        Offer offer1 = new Offer(date1);

        String dateInString2 = "2037-01-01";
        Date date2 = formatter.parse(dateInString2);
        Offer offer2 = new Offer(date2);

        List<Offer> offers = new ArrayList<>();
        offers.add(offer1);
        offers.add(offer2);

        when(offerRepository.findAll()).thenReturn(offers);
        assertEquals(offers, offerService.findAll());
        verify(offerRepository).findAll();
    }

    @Test
    public void saveOfferTest_ShouldVerifySaveMethodOfOfferRepository() {
        Offer offer = new Offer();
        offerService.save(offer);
        verify(offerRepository).save(offer);
    }

    @Test
    public void updateOfferTest_ShouldVerifySaveMethodOfOfferRepository() {
        Offer offer = new Offer();;
        offerService.update(2, offer);
        verify(offerRepository).save(offer);
    }

    @Test
    public void deleteOfferServiceTest_ShouldVerifyDeleteMethodOfOfferRepository() {
        offerService.delete(1);
        verify(offerRepository).deleteById(1);
    }
}
