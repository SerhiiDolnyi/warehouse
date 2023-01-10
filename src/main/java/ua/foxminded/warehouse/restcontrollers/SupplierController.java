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
import ua.foxminded.warehouse.dto.SupplierDTO;
import ua.foxminded.warehouse.models.Supplier;
import ua.foxminded.warehouse.services.AddressService;
import ua.foxminded.warehouse.services.SupplierService;
import ua.foxminded.warehouse.util.exception.address.AddressNotCreatedException;
import ua.foxminded.warehouse.util.exception.address.AddressNotFoundException;
import ua.foxminded.warehouse.util.exception.address.AddressNotUpdatedException;
import ua.foxminded.warehouse.util.exception.supplier.SupplierNotCreatedException;
import ua.foxminded.warehouse.util.exception.supplier.SupplierNotFoundException;
import ua.foxminded.warehouse.util.exception.supplier.SupplierNotUpdatedException;
import ua.foxminded.warehouse.util.validator.SupplierValidatior;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Path.SUPPLIER)
public class SupplierController {
    private SupplierService supplierService;
    private AddressService addressService;
    private ModelMapper modelMapper;
    private SupplierValidatior supplierValidatior;
    private static final Logger log = LoggerFactory.getLogger(SupplierController.class);

    @Autowired
    public SupplierController(SupplierService supplierService, AddressService addressService,
                              ModelMapper modelMapper, SupplierValidatior supplierValidatior) {
        this.supplierService = supplierService;
        this.addressService = addressService;
        this.modelMapper = modelMapper;
        this.supplierValidatior = supplierValidatior;
    }

    @Operation(summary = "Get all Suppliers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Suppliers found successfully"),
            @ApiResponse(responseCode = "404", description = "Suppliers not found")
    })
    @GetMapping(Path.GET_ALL_SUPPLIER)
    public ResponseEntity<List<SupplierDTO>> showAll() {
        log.info("Searching all suppliers ");
        List<SupplierDTO> suppliers = supplierService.findAll().stream().map(this::convertToSupplierDTO)
                .collect(Collectors.toList());
        if(suppliers.isEmpty()) {
            log.info("List of suppliers is empty...");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Suppliers found successfully");

        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }

    @Operation(summary = "Get an Supplier by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Supplier"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @GetMapping(Path.GET_SUPPLIER_BY_ID)
    public ResponseEntity<SupplierDTO> getSupplierById(
            @Parameter(description = "ID of Supplier to be searched") @PathVariable("id") int id) {
        log.info("Searching Supplier by ID");
        SupplierDTO supplierDTO;
        try {
            supplierDTO = convertToSupplierDTO(supplierService.findById(id));
        } catch (SupplierNotFoundException e) {
            log.info("Supplier not found..." + e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Supplier found successfully");

        return new ResponseEntity<>(supplierDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create a new Supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New Supplier added successfully"),
            @ApiResponse(responseCode = "400", description = "Request to create has error")
    })
    @PostMapping(Path.CREATE_SUPPLIER)
    public ResponseEntity<String> createSupplier (@RequestBody @Valid SupplierDTO supplierDTO, BindingResult bindingResult) {
        log.info("Creating new supplier");
        supplierValidatior.validate(supplierDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            String errorMsg = errorMessageBuilder(bindingResult);
            log.info("Error occure ..." + new SupplierNotCreatedException(errorMsg));
            return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        Supplier supplier = convertToSupplier(supplierDTO);
        supplier.setAddress(addressService.findById(supplierDTO.getAddressId()));
        supplierService.save(supplier);
        log.info("New supplier added successfully");

        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Update the Supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier updated successfully"),
            @ApiResponse(responseCode = "400", description = "Request to update has error"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @PutMapping(Path.UPDATE_SUPPLIER)
    public ResponseEntity<String> update(@RequestBody @Valid SupplierDTO supplierDTO, BindingResult bindingResult,
                                         @Parameter(description = "ID of Supplier to be updated") @PathVariable("id") int id) {
        log.info("Updating supplier with ID " + id);
        supplierService.findById(id);
        supplierValidatior.validate(supplierDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            String errorMsg = errorMessageBuilder(bindingResult);
            log.info("Error occure ..." + new SupplierNotUpdatedException(errorMsg));
            return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        Supplier supplier = convertToSupplier(supplierDTO);
        supplier.setAddress(addressService.findById(supplierDTO.getAddressId()));
        supplierService.update(id, supplier);
        log.info("Supplier updated successfully! ");

        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Delete Supplier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found")
    })
    @DeleteMapping(Path.DELETE_SUPPLIER_BY_ID)
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        log.info("Deleting supplier");
        try {
            supplierService.findById(id);
        }   catch (SupplierNotFoundException e) {
            log.info("Supplier not found..." + e);
            return new ResponseEntity<>("Supplier not found", HttpStatus.NOT_FOUND);
        }
        supplierService.delete(id);
        log.info("Supplier deleted successfully! ");

        return new ResponseEntity(HttpStatus.OK);
    }

    private Supplier convertToSupplier(SupplierDTO supplierDTO) {
        return modelMapper.map(supplierDTO, Supplier.class);
    }

    private SupplierDTO convertToSupplierDTO(Supplier supplier) {
        return modelMapper.map(supplier, SupplierDTO.class);
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
