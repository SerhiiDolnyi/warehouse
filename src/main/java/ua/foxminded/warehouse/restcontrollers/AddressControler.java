package ua.foxminded.warehouse.restcontrollers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ua.foxminded.warehouse.dto.AddressDTO;
import ua.foxminded.warehouse.models.Address;
import ua.foxminded.warehouse.services.AddressService;
import ua.foxminded.warehouse.util.exception.address.AddressNotCreatedException;
import ua.foxminded.warehouse.util.exception.address.AddressNotFoundException;
import ua.foxminded.warehouse.util.exception.address.AddressNotUpdatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Path.ADDRESS)
public class AddressControler {
    @Autowired
    private AddressService addressService;
    private ModelMapper modelMapper;
    private static final Logger log = LoggerFactory.getLogger(AddressControler.class);

    @Autowired
    public AddressControler(AddressService addressService, ModelMapper modelMapper) {
        this.addressService = addressService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get all addresses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of addresses found successfully"),
            @ApiResponse(responseCode = "404", description = "Addresses not found")
    })
    @GetMapping(Path.GET_ALL_ADDRESS)
    public ResponseEntity<List<AddressDTO>> showAllAddressesDTO() {
        log.info("Searching all addresses ");
        List<AddressDTO> addresses = addressService.findAll().stream().map(this::convertToAddressDTO)
                    .collect(Collectors.toList());
        if(addresses.isEmpty()) {
            log.info("List of Addresses is empty...");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Addresses found successfully! ");

        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @Operation(summary = "Get an address by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found address"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @GetMapping(Path.GET_ADDRESS_BY_ID)
    public ResponseEntity<AddressDTO> getAddressDTOById(
            @Parameter(description = "ID of address to be searched") @PathVariable("id") int id) {
        log.info("Searching address by ID");
        AddressDTO addressDTO;
        try {
            addressDTO = convertToAddressDTO(addressService.findById(id));
        } catch (AddressNotFoundException e) {
            log.info("Address not found..." + e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Address found successfully! ");

        return new ResponseEntity<AddressDTO>(addressDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create a new address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New address added successfully"),
            @ApiResponse(responseCode = "400", description = "Request to create has error")
    })
    @PostMapping(Path.CREATE_ADDRESS)
    public ResponseEntity<String> createAddress(@RequestBody @Valid AddressDTO addressDTO, BindingResult bindingResult) {
        log.info("Creating new address");
        if(bindingResult.hasErrors()) {
            String errorMsg = errorMessageBuilder(bindingResult);
            log.info("Error occur ..." + new AddressNotCreatedException(errorMsg));
            return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        addressService.save(convertToAddress(addressDTO));
        log.info("New address added successfully! ");

        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Update the address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "400", description = "Request to update has error"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @PutMapping(Path.UPDATE_ADDRESS)
    public ResponseEntity<String> update(@RequestBody @Valid AddressDTO addressDTO, BindingResult bindingResult,
                                         @Parameter(description = "ID of address to be updated") @PathVariable("id") int id) {
        log.info("Updating address with ID " + id);
        addressService.findById(id);
        if(bindingResult.hasErrors()) {
            String errorMsg = errorMessageBuilder(bindingResult);
            log.info("Error occure ..." + new AddressNotUpdatedException(errorMsg));
            return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        addressService.update(id, convertToAddress(addressDTO));
        log.info("Address updated successfully! ");

        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Delete address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @DeleteMapping(Path.DELETE_ADDRESS_BY_ID)
    public ResponseEntity<String> delete(@Parameter(description = "ID of address to be deleted") @PathVariable("id") int id) {
        log.info("Deleting address");
        try {
            addressService.findById(id);
        }   catch (AddressNotFoundException e) {
            log.info("Address not found..." + e);
            return new ResponseEntity<>("Address not found", HttpStatus.NOT_FOUND);
        }
        addressService.delete(id);
        log.info("Address deleted successfully! ");

        return new ResponseEntity(HttpStatus.OK);
    }

    private AddressDTO convertToAddressDTO(Address address) {
        return modelMapper.map(address, AddressDTO.class);
    }

    private Address convertToAddress(AddressDTO addressDTO) {
        return modelMapper.map(addressDTO, Address.class);
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
