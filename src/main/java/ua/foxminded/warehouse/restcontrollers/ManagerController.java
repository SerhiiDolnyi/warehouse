package ua.foxminded.warehouse.restcontrollers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.warehouse.dto.CustomerDTO;
import ua.foxminded.warehouse.dto.InvoiceDTO;
import ua.foxminded.warehouse.dto.ItemDTO;
import ua.foxminded.warehouse.dto.OfferDTO;
import ua.foxminded.warehouse.models.*;
import ua.foxminded.warehouse.services.ManagerService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Path.MANAGER)
public class ManagerController {
    private ManagerService managerService;
    private ModelMapper modelMapper;
    private static final Logger log = LoggerFactory.getLogger(AddressControler.class);

    @Autowired
    public ManagerController(ManagerService managerService, ModelMapper modelMapper) {
        this.managerService = managerService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get all Items By Amount Less Than")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Items found successfully"),
            @ApiResponse(responseCode = "404", description = "Items not found")
    })
    @GetMapping(Path.ITEM_BY_AMOUNT_LESS_THAN)
    @ResponseBody
    public ResponseEntity<List<ItemDTO>> showItemByAmountLessThan(
            @Parameter(description = "Item Amount for searching") @RequestParam(name="itemAmount") int itemAmount) {
        log.info("Searching all Items By Amount Less Than ... ");
        List<ItemDTO> items = managerService.findItemByAmountLessThan(itemAmount).stream().map(this::convertToItemDTO)
                .collect(Collectors.toList());
        if(items.isEmpty()) {
            log.info("List of Items is empty...");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Items found successfully! ");

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @Operation(summary = "Get all Invoices By Customer Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Invoices found successfully"),
            @ApiResponse(responseCode = "404", description = "Invoices not found")
    })
    @GetMapping(Path.INVOICES_BY_CUSTOMER_ID)
    @ResponseBody
    public ResponseEntity<List<InvoiceDTO>> showInvoicesByCustomerId(
            @Parameter(description = "Customer Id for searching") @RequestParam(name="customerId") int customerId) {
        log.info("Searching Invoices By Customer Id... ");
        List<InvoiceDTO> invoices = managerService.findInvoicesByCustomerId(customerId).stream().map(this::convertToInvoiceDTO)
                .collect(Collectors.toList());
        if(invoices.isEmpty()) {
            log.info("List of Invoices is empty...");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Invoices found successfully! ");

        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @Operation(summary = "Get all offers By Supplier Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of offers found successfully"),
            @ApiResponse(responseCode = "404", description = "offers not found")
    })
    @GetMapping(Path.OFFER_BY_SUPPLIER_ID)
    @ResponseBody
    public ResponseEntity<List<OfferDTO>> showOfferBySupplierId(
            @Parameter(description = "Supplier Id for searching") @RequestParam(name="supplierId") int supplierId, Model model) {
        log.info("Searching all offers By Supplier Id... ");
        List<OfferDTO> offers = managerService.findOfferBySupplierId(supplierId).stream().map(this::convertToOfferDTO)
                .collect(Collectors.toList());
        if(offers.isEmpty()) {
            log.info("List of offers is empty...");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Offers found successfully! ");

        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @Operation(summary = "First Three Customers By The Most Expensive Invoices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Customers found successfully"),
            @ApiResponse(responseCode = "404", description = "Customers not found")
    })
    @GetMapping(Path.FIRST_3_CUSTOMERS_BY_MOST_EXPENSIVE_INVOICE)
    @ResponseBody
    public ResponseEntity<List<CustomerDTO>> findFirst3CustomerByTheMostExpensiveInvoice () {
        log.info("Searching First Three Customers By The Most Expensive Invoices... ");
        List<CustomerDTO> customers = managerService.findFirst3CustomerByTheMostExpensiveInvoice().stream().map(this::convertToCustomerTDO)
                .collect(Collectors.toList());
        if(customers.isEmpty()) {
            log.info("List of customers is empty...");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Customers found successfully! ");

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Operation(summary = "Find Invoices By Customer Rate And Price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Invoices found successfully"),
            @ApiResponse(responseCode = "404", description = "Invoices not found")
    })
    @GetMapping(Path.INVOICES_BY_CUSTOMERRATE_AND_PRICE)
    @ResponseBody
    public ResponseEntity<List<InvoiceDTO>> findInvoicesByCustomerRateAndPrice (
            @Parameter(description = "Customer Rate for searching") @RequestParam(name="customerRate") int customerRate,
            @Parameter(description = "Price for searching") @RequestParam(name="price") BigDecimal price) {
        log.info("Searching Invoices By Customer Rate And Price... ");
        List<InvoiceDTO> invoices = managerService.findInvoicesByCustomerRateAndPrice(customerRate, price).stream()
                .map(this::convertToInvoiceDTO).collect(Collectors.toList());
        if(invoices.isEmpty()) {
            log.info("List of Invoices is empty...");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Invoices found successfully! ");

        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @Operation(summary = "Find Offers By Price And Supplier City")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Offers found successfully"),
            @ApiResponse(responseCode = "404", description = "Offers not found")
    })
    @GetMapping(Path.OFFERS_BY_PRICE_AND_SUPPLIERCITY)
    @ResponseBody
    public ResponseEntity<List<OfferDTO>> findOffersByPriceAndSupplierCity(
            @Parameter(description = "Price for searching") @RequestParam(name="price") BigDecimal price,
            @Parameter(description = "City for searching") @RequestParam(name="city") String city) {
        log.info("Searching Offers By Price And Supplier City... ");
        List<OfferDTO> offers = managerService.findOffersByPriceAndSupplierCity(price, city).stream()
                .map(this::convertToOfferDTO).collect(Collectors.toList());
        if(offers.isEmpty()) {
            log.info("List of Offers is empty...");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Offers found successfully! ");

        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    private ItemDTO convertToItemDTO(Item item) {
        return modelMapper.map(item, ItemDTO.class);
    }

    private InvoiceDTO convertToInvoiceDTO(Invoice invoice) {
        return modelMapper.map(invoice, InvoiceDTO.class);
    }

    private OfferDTO convertToOfferDTO(Offer offer) {
        return modelMapper.map(offer, OfferDTO.class);
    }

    private CustomerDTO convertToCustomerTDO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }
}
