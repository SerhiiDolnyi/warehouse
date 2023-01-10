package ua.foxminded.warehouse.invoice;


import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import ua.foxminded.warehouse.models.*;
import ua.foxminded.warehouse.repositories.*;
import ua.foxminded.warehouse.services.InvoiceService;
import ua.foxminded.warehouse.services.SupplierService;

import javax.xml.crypto.Data;
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
public class InvoiceDBIntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Value("${sql.script.create.address}")
    private String sqlAddAddress;

    @Value("${sql.script.create.address3}")
    private String sqlAddAddress3;

    @Value("${sql.script.create.customer2}")
    private String sqlAddCustomer2;

    @Value("${sql.script.create.customer3}")
    private String sqlAddCustomer3;

    @Value("${sql.script.create.item2}")
    private String sqlAddItem2;

    @Value("${sql.script.create.item3}")
    private String sqlAddItem3;

    @Value("${sql.script.create.invoice2}")
    private String sqlAddInvoice2;

    @Value("${sql.script.create.invoice3}")
    private String sqlAddInvoice3;


    @Value("${sql.script.delete.address}")
    private String sqlDeleteAddresses;

    @Value("${sql.script.delete.customer}")
    private String sqlDeleteCustomer;

    @Value("${sql.script.delete.item}")
    private String sqlDeleteItem;

    @Value("${sql.script.delete.invoice}")
    private String sqlDeleteInvoice;

    @BeforeEach
    public void setupDatabase() {
        jdbcTemplate.execute(sqlAddAddress);
        jdbcTemplate.execute(sqlAddCustomer2);
        jdbcTemplate.execute(sqlAddItem2);
        jdbcTemplate.execute(sqlAddInvoice2);
    }

    @AfterEach
    public void setupAfterTransaction(){
        jdbcTemplate.execute(sqlDeleteInvoice);
        jdbcTemplate.execute(sqlDeleteCustomer);
        jdbcTemplate.execute(sqlDeleteItem);
        jdbcTemplate.execute(sqlDeleteAddresses);
    }

    @Test
    public void findAllInvoices_ShouldReturnSizeOfList() {
        jdbcTemplate.execute(sqlAddAddress3);
        jdbcTemplate.execute(sqlAddCustomer3);
        jdbcTemplate.execute(sqlAddItem3);
        jdbcTemplate.execute(sqlAddInvoice3);
        List<Invoice> invoices = invoiceService.findAll();
        assertEquals(2, invoices.size());
    }

    @ParameterizedTest
    @CsvSource({"0, 2", "1, 3"})
    public void findAllInvoices_ShouldReturnItemCount(int index, int itemCount) {
        jdbcTemplate.execute(sqlAddAddress3);
        jdbcTemplate.execute(sqlAddCustomer3);
        jdbcTemplate.execute(sqlAddItem3);
        jdbcTemplate.execute(sqlAddInvoice3);
        List<Invoice> invoices = invoiceService.findAll();
        Invoice invoice = invoices.get(index);
        assertEquals(itemCount, invoice.getItemCount());
    }

    @Test
    public void findInvoiceByIdTest_ShouldReturnItemCountOfFoundInvoice() {
        Invoice foundInvoice = invoiceService.findById(2);
        assertEquals(2, foundInvoice.getItemCount());
    }

    @Test
    public void saveInvoiceTest_ShouldReturnDateOfSavedInvoice() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2025-01-01";
        Date date = formatter.parse(dateInString);
        Customer customer = customerRepository.findById(2).get();
        Item item = itemRepository.findById(2).get();

        Invoice invoice = new Invoice(date, customer, item, BigDecimal.valueOf(200.00), 1, "Registered");
        invoiceService.save(invoice);
        Invoice savedInvoice = invoiceRepository.findByDate(date);
        assertEquals(date, savedInvoice.getDate());
    }

    @Test
    public void deleteInvoiceTest_ShouldReturnFalse(){
        Optional<Invoice> deletedInvoice = invoiceRepository.findById(2);
        assertTrue(deletedInvoice.isPresent());
        invoiceService.delete(2);
        deletedInvoice = invoiceRepository.findById(2);
        assertFalse(deletedInvoice.isPresent());
    }

    @Test
    public void updateInvoiceTest_ShouldReturnUpdatedDate() throws ParseException {
        Optional<Invoice> invoice = invoiceRepository.findById(2);
        assertTrue(invoice.isPresent());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2030-01-01";
        Date date = formatter.parse(dateInString);
        Customer customer = customerRepository.findById(2).get();
        Item item = itemRepository.findById(2).get();
        Invoice updatedInvoice = new Invoice(date, customer, item, BigDecimal.valueOf(200.00), 1, "Registered");

        invoiceService.update(2, updatedInvoice);
        Invoice savedInvoice = invoiceRepository.findById(2).get();
        assertEquals(date, savedInvoice.getDate());
    }
}
