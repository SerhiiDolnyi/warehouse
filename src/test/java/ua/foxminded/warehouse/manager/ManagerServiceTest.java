package ua.foxminded.warehouse.manager;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.warehouse.models.Customer;
import ua.foxminded.warehouse.models.Invoice;
import ua.foxminded.warehouse.models.Item;
import ua.foxminded.warehouse.models.Offer;
import ua.foxminded.warehouse.repositories.CustomerRepository;
import ua.foxminded.warehouse.repositories.InvoiceRepository;
import ua.foxminded.warehouse.repositories.ItemRepository;
import ua.foxminded.warehouse.repositories.OfferRepository;
import ua.foxminded.warehouse.services.ManagerService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
public class ManagerServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private OfferRepository offerRepository;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private ManagerService managerService;

    @Test
    public void findItemByAmountLessThanTest_ShouldReturnListOfItem() {
        List<Item> items = new ArrayList<>();
        when(itemRepository.findItemByAmountLessThan(Mockito.anyInt())).thenReturn(items);
        assertEquals(items, managerService.findItemByAmountLessThan(Mockito.anyInt()));
        verify(itemRepository).findItemByAmountLessThan(Mockito.anyInt());
    }

    @Test
    public void findInvoicesByCustomerIdTest_ShouldReturnListOfInvoice() {
        List<Invoice> invoices = new ArrayList<>();
        when(invoiceRepository.findInvoicesByCustomerId(Mockito.anyInt())).thenReturn(invoices);
        assertEquals(invoices, managerService.findInvoicesByCustomerId(Mockito.anyInt()));
        verify(invoiceRepository).findInvoicesByCustomerId(Mockito.anyInt());
    }

    @Test
    public void findInvoicesByCustomerIdTest2_ShouldReturnListOfInvoice() {
        List<Invoice> invoices = new ArrayList<>();
        when(invoiceRepository.findInvoicesByCustomerId(Mockito.anyInt())).thenReturn(invoices);
        assertEquals(invoices, managerService.findInvoicesByCustomerId(Mockito.anyInt()));
        verify(invoiceRepository).findInvoicesByCustomerId(Mockito.anyInt());
    }

    @Test
    public void findOfferBySupplierIdTest_ShouldReturnListOfOffer() {
        List<Offer> offers = new ArrayList<>();
        when(offerRepository.findOfferBySupplierId(Mockito.anyInt())).thenReturn(offers);
        assertEquals(offers, managerService.findOfferBySupplierId(Mockito.anyInt()));
        verify(offerRepository).findOfferBySupplierId(Mockito.anyInt());
    }

    @Test
    public void findFirst3CustomerByTheMostExpensiveInvoiceTest_ShouldReturnListOfCustomers() {
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findFirst3CustomerByTheMostExpensiveInvoice()).thenReturn(customers);
        assertEquals(customers, managerService.findFirst3CustomerByTheMostExpensiveInvoice());
        verify(customerRepository).findFirst3CustomerByTheMostExpensiveInvoice();
    }

    @Test
    public void findInvoicesByCustomerRateAndPriceTest_ShouldReturnListOfInvoices() throws ParseException {
        List<Invoice> invoices = new ArrayList<>();
        when(invoiceRepository.findInvoicesByCustomerRateAndPrice(Mockito.anyInt(), eq(BigDecimal.valueOf(200.00)))).thenReturn(invoices);
        assertEquals(invoices, managerService.findInvoicesByCustomerRateAndPrice(Mockito.anyInt(), eq(BigDecimal.valueOf(200.00))));
        verify(invoiceRepository).findInvoicesByCustomerRateAndPrice(Mockito.anyInt(), eq(BigDecimal.valueOf(200.00)));
    }

    @Test
    public void findOffersByPriceAndSupplierCityTest_ShouldReturnListOfOffers() {
        List<Offer> offers = new ArrayList<>();
        when(offerRepository.findOffersByPriceAndSupplierCity(eq(BigDecimal.valueOf(200.00)), Mockito.anyString())).thenReturn(offers);
        assertEquals(offers, managerService.findOffersByPriceAndSupplierCity(eq(BigDecimal.valueOf(200.00)), Mockito.anyString()));
        verify(offerRepository).findOffersByPriceAndSupplierCity(eq(BigDecimal.valueOf(200.00)), Mockito.anyString());
    }
}
