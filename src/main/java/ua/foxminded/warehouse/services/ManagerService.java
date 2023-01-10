package ua.foxminded.warehouse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.warehouse.models.*;
import ua.foxminded.warehouse.repositories.CustomerRepository;
import ua.foxminded.warehouse.repositories.InvoiceRepository;
import ua.foxminded.warehouse.repositories.ItemRepository;
import ua.foxminded.warehouse.repositories.OfferRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ManagerService {
    private ItemRepository itemRepository;
    private InvoiceRepository invoiceRepository;
    private OfferRepository offerRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public ManagerService(ItemRepository itemRepository, InvoiceRepository invoiceRepository,
                          OfferRepository offerRepository, CustomerRepository customerRepository) {
        this.itemRepository = itemRepository;
        this.invoiceRepository = invoiceRepository;
        this.offerRepository = offerRepository;
        this.customerRepository = customerRepository;
    }

    public List<Item> findItemByAmountLessThan(int amount){
        return itemRepository.findItemByAmountLessThan(amount);
    }

    public List<Invoice> findInvoicesByCustomerId(int customerId) {
        return invoiceRepository.findInvoicesByCustomerId(customerId);
    }

    public List<Offer> findOfferBySupplierId(int supplierId) {
        return offerRepository.findOfferBySupplierId(supplierId);
    }

    /*Query to select first three customers, who has invoices with the most expensive price*/
    public List<Customer> findFirst3CustomerByTheMostExpensiveInvoice() {
        return customerRepository.findFirst3CustomerByTheMostExpensiveInvoice();
    }

    /*Query to find all invoices which has customer_rate of customer more then X and price of item more than Y*/
    public List<Invoice> findInvoicesByCustomerRateAndPrice(int customerRate, BigDecimal price) {
        return invoiceRepository.findInvoicesByCustomerRateAndPrice(customerRate, price);
    }

    /*Query to find all offers which has price of item more than X and supplier from city Y*/
    public List<Offer> findOffersByPriceAndSupplierCity(BigDecimal price, String city) {
        return offerRepository.findOffersByPriceAndSupplierCity(price, city);
    }
}
