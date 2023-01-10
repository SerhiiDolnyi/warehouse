package ua.foxminded.warehouse.manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ua.foxminded.warehouse.models.Customer;
import ua.foxminded.warehouse.models.Invoice;
import ua.foxminded.warehouse.models.Item;
import ua.foxminded.warehouse.models.Offer;
import ua.foxminded.warehouse.repositories.CustomerRepository;
import ua.foxminded.warehouse.repositories.InvoiceRepository;
import ua.foxminded.warehouse.repositories.ItemRepository;
import ua.foxminded.warehouse.repositories.OfferRepository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class ManagerDBIntegrationTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findItemByAmountLessThanTest_ShouldReturnListOfItem() {
        List<Item> items = itemRepository.findItemByAmountLessThan(5);
        assertEquals(4, items.size());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findInvoicesByCustomerIdTest_ShouldReturnListOfInvoice() {
        List<Invoice> invoices = invoiceRepository.findInvoicesByCustomerId(1);
        assertEquals(1, invoices.size());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findOfferBySupplierIdTest_ShouldReturnListOfOffer() {
        List<Offer> offers = offerRepository.findOfferBySupplierId(1);
        assertEquals(1, offers.size());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findFirst3CustomerByTheMostExpensiveInvoiceTest_ShouldReturnListOfThreeCustomers() {
        List<Customer> customers = customerRepository.findFirst3CustomerByTheMostExpensiveInvoice();
        assertEquals(3, customers.size());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findInvoicesByCustomerRateAndPriceTest_ShouldReturnListOfInvoices() throws ParseException {
        List<Invoice> invoices = invoiceRepository.findInvoicesByCustomerRateAndPrice(2, BigDecimal.valueOf(200.00));
        assertEquals(1, invoices.size());
    }

    @Test
    @Sql({"/drop-schema.sql", "/schema.sql"})
    @Sql({"/data.sql"})
    public void findOffersByPriceAndSupplierCityTest_ShouldReturnListOfOffers() {
        List<Offer> offers = offerRepository.findOffersByPriceAndSupplierCity(BigDecimal.valueOf(10.00), "Kyiv");
        assertEquals(1, offers.size());
    }
}
