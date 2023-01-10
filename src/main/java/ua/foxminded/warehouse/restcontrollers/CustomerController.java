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
import ua.foxminded.warehouse.dto.CustomerDTO;
import ua.foxminded.warehouse.models.Customer;
import ua.foxminded.warehouse.services.AddressService;
import ua.foxminded.warehouse.services.CustomerService;
import ua.foxminded.warehouse.util.exception.address.AddressNotCreatedException;
import ua.foxminded.warehouse.util.exception.address.AddressNotFoundException;
import ua.foxminded.warehouse.util.exception.address.AddressNotUpdatedException;
import ua.foxminded.warehouse.util.exception.customer.CustomerNotCreatedException;
import ua.foxminded.warehouse.util.exception.customer.CustomerNotFoundException;
import ua.foxminded.warehouse.util.exception.customer.CustomerNotUpdatedException;
import ua.foxminded.warehouse.util.validator.CustomerValidator;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Path.CUSTOMER)
public class CustomerController {
    private CustomerService customerService;
    private ModelMapper modelMapper;
    private AddressService addressService;
    private CustomerValidator customerValidator;
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper modelMapper,
                              AddressService addressService, CustomerValidator customerValidator) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
        this.addressService = addressService;
        this.customerValidator = customerValidator;
    }

    @Operation(summary = "Get all customers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of customers found successfully"),
            @ApiResponse(responseCode = "404", description = "Customers not found")
    })
    @GetMapping(Path.GET_ALL_CUSTOMER)
    public ResponseEntity<List<CustomerDTO>> showAllCustomerDTO() {
        log.info("Searching all customers ");
        List<CustomerDTO> customers = customerService.findAll().stream().map(this::convertToCustomerTDO)
                .collect(Collectors.toList());
        if(customers.isEmpty()) {
            log.info("List of Customers is empty...");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Customers found successfully");

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @Operation(summary = "Get an customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found customer"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping(Path.GET_CUSTOMER_BY_ID)
    public ResponseEntity<CustomerDTO> getCustomerDTOById(
            @Parameter(description = "ID of customer to be searched") @PathVariable("id") int id) {
        log.info("Searching customer by ID");
        CustomerDTO customerDTO;
        try {
            customerDTO = convertToCustomerTDO(customerService.findById(id));
        } catch (CustomerNotFoundException e) {
            log.info("Customer not found..." + e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Customer found successfully");

        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }
    @Operation(summary = "Create a new customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New customer added successfully"),
            @ApiResponse(responseCode = "400", description = "Request to create has error")
    })
    @PostMapping(Path.CREATE_CUSTOMER)
    public ResponseEntity<String> createCustomer (@RequestBody @Valid CustomerDTO customerDTO, BindingResult bindingResult) {
        log.info("Creating new address");
        customerValidator.validate(customerDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            String errorMsg = errorMessageBuilder(bindingResult);
            log.info("Error occure ..." + new CustomerNotCreatedException(errorMsg));
            return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        Customer customer = convertToCustomer(customerDTO);
        customer.setAddress(addressService.findById(customerDTO.getAddressId()));
        customerService.save(customer);
        log.info("New customer added successfully");

        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Update the customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сustomer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Request to update has error"),
            @ApiResponse(responseCode = "404", description = "Сustomer not found")
    })
    @PutMapping(Path.UPDATE_CUSTOMER)
    public ResponseEntity<String> update(@RequestBody @Valid CustomerDTO customerDTO, BindingResult bindingResult,
                                         @Parameter(description = "ID of customer to be updated") @PathVariable("id") int id) {
        log.info("Updating customer with ID " + id);
        customerService.findById(id);
        customerValidator.validate(customerDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            String errorMsg = errorMessageBuilder(bindingResult);
            log.info("Error occure ..." + new CustomerNotUpdatedException(errorMsg));
            return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        Customer customer = convertToCustomer(customerDTO);
        customer.setAddress(addressService.findById(customerDTO.getAddressId()));
        customerService.update(id, customer);
        log.info("Customer updated successfully");

        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Delete customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сustomer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Сustomer not found")
    })
    @DeleteMapping(Path.DELETE_CUSTOMER_BY_ID)
    public ResponseEntity<String> delete(
            @Parameter(description = "ID of customer to be deleted") @PathVariable("id") int id) {
        log.info("Deleting customer");
        try {
            customerService.findById(id);
        }   catch (CustomerNotFoundException e) {
            log.info("Customer not found..." + e);
            return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
        }
        customerService.delete(id);
        log.info("Customer deleted successfully");

        return new ResponseEntity(HttpStatus.OK);
    }

    private CustomerDTO convertToCustomerTDO(Customer customer) {
        return modelMapper.map(customer, CustomerDTO.class);
    }

    private Customer convertToCustomer(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, Customer.class);
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
