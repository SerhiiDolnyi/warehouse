package ua.foxminded.warehouse.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.warehouse.models.Customer;
import ua.foxminded.warehouse.models.Invoice;
import ua.foxminded.warehouse.models.Item;
import ua.foxminded.warehouse.models.Stage;
import ua.foxminded.warehouse.repositories.InvoiceRepository;
import ua.foxminded.warehouse.util.exception.invoice.InvoiceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class InvoiceService {
    InvoiceRepository invoiceRepository;
    ItemService itemService;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> findAll(){
        return invoiceRepository.findAll();
    }

    public Invoice findById(int invoiceId){
        Optional<Invoice> foundInvoice = invoiceRepository.findById(invoiceId);
        return foundInvoice.orElseThrow(InvoiceNotFoundException::new);
    }

    @Transactional
    public void save (Invoice invoice){
        invoiceRepository.save(invoice);
    }

    @Transactional
    public void update (int invoiceId, Invoice updatedInvoice){
        updatedInvoice.setId(invoiceId);
        invoiceRepository.save(updatedInvoice);
    }

    @Transactional
    public void delete (int invoiceId){
        invoiceRepository.deleteById(invoiceId);
    }
}
