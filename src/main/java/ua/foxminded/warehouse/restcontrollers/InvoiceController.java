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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.warehouse.dto.AddressDTO;
import ua.foxminded.warehouse.dto.InvoiceDTO;
import ua.foxminded.warehouse.models.Invoice;
import ua.foxminded.warehouse.services.CustomerService;
import ua.foxminded.warehouse.services.InvoiceService;
import ua.foxminded.warehouse.services.ItemService;
import ua.foxminded.warehouse.util.exception.address.AddressNotCreatedException;
import ua.foxminded.warehouse.util.exception.address.AddressNotFoundException;
import ua.foxminded.warehouse.util.exception.address.AddressNotUpdatedException;
import ua.foxminded.warehouse.util.exception.invoice.InvoiceNotCreatedException;
import ua.foxminded.warehouse.util.exception.invoice.InvoiceNotFoundException;
import ua.foxminded.warehouse.util.exception.item.ItemNotUpdatedException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Path.INVOICE)
public class InvoiceController {
    private  InvoiceService invoiceService;
    private ModelMapper modelMapper;
    private CustomerService customerService;
    private ItemService itemService;
    private static final Logger log = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    public InvoiceController(InvoiceService invoiceService, ModelMapper modelMapper,
                             CustomerService customerService, ItemService itemService) {
        this.invoiceService = invoiceService;
        this.modelMapper = modelMapper;
        this.customerService = customerService;
        this.itemService = itemService;
    }

    @Operation(summary = "Get all Invoices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Invoices found successfully"),
            @ApiResponse(responseCode = "404", description = "Invoices not found")
    })
    @GetMapping(Path.GET_ALL_INVOICE)
    public ResponseEntity<List<InvoiceDTO>> showAll() {
        log.info("Searching all Invoices ");
        List<InvoiceDTO> invoices = invoiceService.findAll().stream().map(this::convertToInvoiceDTO)
                .collect(Collectors.toList());
        if(invoices.isEmpty()) {
            log.info("List of invoices is empty...");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Invoices found successfully! ");

        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @Operation(summary = "Get an Invoice by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Invoices"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @GetMapping(Path.GET_INVOICE_BY_ID)
    public ResponseEntity<InvoiceDTO> getInvoiceById(
            @Parameter(description = "ID of Invoice to be searched") @PathVariable("id") int id) {
        log.info("Searching Invoice by ID");
        InvoiceDTO invoiceDTO;
        try {
            invoiceDTO = convertToInvoiceDTO(invoiceService.findById(id));
        } catch (InvoiceNotFoundException e) {
            log.info("Invoice not found..." + e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Invoice found successfully! ");

        return new ResponseEntity<>(invoiceDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create a new Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New Invoice added successfully"),
            @ApiResponse(responseCode = "400", description = "Request to create has error")
    })
    @PostMapping(Path.CREATE_INVOICE)
    public ResponseEntity<String> createInvoice (@RequestBody @Valid InvoiceDTO invoiceDTO, BindingResult bindingResult) {
        log.info("Creating new Invoice. ");
        if(bindingResult.hasErrors()) {
            String errorMsg = errorMessageBuilder(bindingResult);
            log.info("Error occure ..." + new InvoiceNotCreatedException(errorMsg));
            return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        Invoice invoice = convertToInvoice(invoiceDTO);
        invoice.setItem(itemService.findById(invoiceDTO.getItemId()));
        invoice.setCustomer(customerService.findById(invoiceDTO.getCustomerId()));
        invoiceService.save(invoice);
        log.info("New Invoice added successfully! ");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Update the Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice updated successfully"),
            @ApiResponse(responseCode = "400", description = "Request to update has error"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @PutMapping(Path.UPDATE_INVOICE)
    public ResponseEntity<String> updateInvoice (@RequestBody @Valid InvoiceDTO invoiceDTO, BindingResult bindingResult,
                                @Parameter(description = "ID of Invoice to be updated") @PathVariable("id") int id) {
        log.info("Updating Invoice with ID " + id);
        invoiceService.findById(id);
        if(bindingResult.hasErrors()) {
            String errorMsg = errorMessageBuilder(bindingResult);
            log.info("Error occure ..." + new ItemNotUpdatedException(errorMsg));
            return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        Invoice invoice = convertToInvoice(invoiceDTO);
        invoice.setItem(itemService.findById(invoiceDTO.getItemId()));
        invoice.setCustomer(customerService.findById(invoiceDTO.getCustomerId()));
        invoiceService.update(id,invoice);
        log.info("Invoice updated successfully! ");

        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Delete Invoice")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Invoice not found")
    })
    @DeleteMapping(Path.DELETE_INVOICE_BY_ID)
    public ResponseEntity<String> delete(
            @Parameter(description = "ID of Invoice to be deleted") @PathVariable("id") int id) {
        log.info("Deleting invoice");
        try {
            invoiceService.findById(id);
        }   catch (InvoiceNotFoundException e) {
            log.info(" Invoice not found..." + e);
            return new ResponseEntity<>("Invoice not found", HttpStatus.NOT_FOUND);
        }
        invoiceService.delete(id);
        log.info("Invoice deleted successfully! ");

        return new ResponseEntity(HttpStatus.OK);
    }

    private Invoice convertToInvoice(InvoiceDTO invoiceDTO) {
        return modelMapper.map(invoiceDTO, Invoice.class);
    }

    private InvoiceDTO convertToInvoiceDTO(Invoice invoice) {
        return modelMapper.map(invoice, InvoiceDTO.class);
    }

    private String errorMessageBuilder(BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            errorMsg.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; ");
        }

        return errorMsg.toString();
    }
}
