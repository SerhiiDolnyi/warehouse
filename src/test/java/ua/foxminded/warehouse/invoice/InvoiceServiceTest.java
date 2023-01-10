package ua.foxminded.warehouse.invoice;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.warehouse.models.Invoice;
import ua.foxminded.warehouse.repositories.InvoiceRepository;
import ua.foxminded.warehouse.services.InvoiceService;

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
public class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    @Test
    public void findByIdTest_ShouldReturnInvoice() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = "2025-01-01";
        Date date = formatter.parse(dateInString);
        Invoice invoice = new Invoice(date);
        when(invoiceRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(invoice));
        assertEquals(invoice, invoiceService.findById(Mockito.anyInt()));
        verify(invoiceRepository).findById(1);
    }

    @Test
    public void findAllTest_ShouldReturnAllInvoices() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString1 = "2025-01-01";
        Date date1 = formatter.parse(dateInString1);
        Invoice invoice1 = new Invoice(date1);

        String dateInString2 = "2026-01-01";
        Date date2 = formatter.parse(dateInString2);
        Invoice invoice2 = new Invoice(date2);

        List<Invoice> invoices = new ArrayList<>();
        invoices.add(invoice1);
        invoices.add(invoice2);

        when(invoiceRepository.findAll()).thenReturn(invoices);
        assertEquals(invoices, invoiceService.findAll());
        verify(invoiceRepository).findAll();
    }

    @Test
    public void saveInvoiceTest_ShouldVerifySaveMethodOfInvoiceRepository() {
        Invoice invoice = new Invoice();
        invoiceService.save(invoice);
        verify(invoiceRepository).save(invoice);
    }

    @Test
    public void updateInvoiceTest_ShouldVerifySaveMethodOfInvoiceRepository() {
        Invoice invoice = new Invoice();
        invoiceService.update(2, invoice);
        verify(invoiceRepository).save(invoice);
    }

    @Test
    public void deleteInvoiceServiceTest_ShouldVerifyDeleteMethodOfInvoiceRepository() {
        invoiceService.delete(1);
        verify(invoiceRepository).deleteById(1);
    }
}
