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
import ua.foxminded.warehouse.dto.OfferDTO;
import ua.foxminded.warehouse.models.Offer;
import ua.foxminded.warehouse.restcontrollers.error.ErrorResponse;
import ua.foxminded.warehouse.services.ItemService;
import ua.foxminded.warehouse.services.OfferService;
import ua.foxminded.warehouse.services.SupplierService;
import ua.foxminded.warehouse.util.exception.address.AddressNotCreatedException;
import ua.foxminded.warehouse.util.exception.address.AddressNotFoundException;
import ua.foxminded.warehouse.util.exception.address.AddressNotUpdatedException;
import ua.foxminded.warehouse.util.exception.item.ItemNotUpdatedException;
import ua.foxminded.warehouse.util.exception.offer.OfferNotCreatedException;
import ua.foxminded.warehouse.util.exception.offer.OfferNotFoundException;
import ua.foxminded.warehouse.util.exception.offer.OfferNotUpdatedException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Path.OFFER)
public class OfferController {
    private OfferService offerService;
    private SupplierService supplierService;
    private ItemService itemService;
    private ModelMapper modelMapper;
    private static final Logger log = LoggerFactory.getLogger(OfferController.class);

    @Autowired
    public OfferController(OfferService offerService, SupplierService supplierService, ItemService itemService,
                           ModelMapper modelMapper) {
        this.offerService = offerService;
        this.supplierService = supplierService;
        this.itemService = itemService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get all Offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Offers found successfully"),
            @ApiResponse(responseCode = "404", description = "Offers not found")
    })
    @GetMapping(Path.GET_ALL_OFFER)
    public ResponseEntity<List<OfferDTO>> showAlloffers() {
        log.info("Searching all offers. ");
        List<OfferDTO> offers = offerService.findAll().stream().map(this::convertToOfferDTO)
                .collect(Collectors.toList());
        if(offers.isEmpty()) {
            log.info("List of offers is empty...");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Offers found successfully! ");

        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @Operation(summary = "Get an Offer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Offer"),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    @GetMapping(Path.GET_OFFER_BY_ID)
    public ResponseEntity<OfferDTO> getOfferById(
            @Parameter(description = "ID of address to be searched") @PathVariable("id") int id) {
        log.info("Searching offer by ID");
        OfferDTO offerDTO;
        try {
            offerDTO = convertToOfferDTO(offerService.findById(id));
        } catch (OfferNotFoundException e) {
            log.info("Offer not found..." + e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Offer found successfully! ");

        return new ResponseEntity<>(offerDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create a new offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New offer added successfully"),
            @ApiResponse(responseCode = "400", description = "Request to create has error")
    })
    @PostMapping(Path.CREATE_OFFER)
    public ResponseEntity<String> createOffer (@RequestBody @Valid OfferDTO offerDTO, BindingResult bindingResult) {
        log.info("Creating new offer");
        if(bindingResult.hasErrors()) {
            String errorMsg = errorMessageBuilder(bindingResult);
            log.info("Error occure ..." + new OfferNotCreatedException(errorMsg));
            return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        Offer offer = convertToOffer(offerDTO);
        offer.setItem(itemService.findById(offerDTO.getItemId()));
        offer.setSupplier(supplierService.findById(offerDTO.getSupplierId()));
        offerService.save(offer);
        log.info("New offer added successfully! ");

        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Update the Offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer updated successfully"),
            @ApiResponse(responseCode = "400", description = "Request to update has error"),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    @PutMapping(Path.UPDATE_OFFER)
    public ResponseEntity<String> updateOffer (@RequestBody @Valid OfferDTO offerDTO, BindingResult bindingResult,
                        @Parameter(description = "ID of Offer to be updated") @PathVariable("id") int id) {
        log.info("Updating offer with ID " + id);
        offerService.findById(id);
        if(bindingResult.hasErrors()) {
            String errorMsg = errorMessageBuilder(bindingResult);
            log.info("Error occure ..." + new OfferNotUpdatedException(errorMsg));
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        Offer offer = convertToOffer(offerDTO);
        offer.setItem(itemService.findById(offerDTO.getItemId()));
        offer.setSupplier(supplierService.findById(offerDTO.getSupplierId()));
        offerService.update(id, offer);
        log.info("Offer updated successfully! ");

        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Delete Offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Offer not found")
    })
    @DeleteMapping(Path.DELETE_OFFER_BY_ID)
    public ResponseEntity<String> delete(
            @Parameter(description = "ID of address to be deleted") @PathVariable("id") int id) {
        log.info("Deleting offer");
        try {
            offerService.findById(id);
        }   catch (OfferNotFoundException e) {
            log.info("Offer not found..." + e);
            return new ResponseEntity<>("Offer not found", HttpStatus.NOT_FOUND);
        }
        offerService.delete(id);
        log.info("Offer deleted successfully! ");

        return new ResponseEntity(HttpStatus.OK);
    }

    private Offer convertToOffer(OfferDTO offerDTO) {
        return modelMapper.map(offerDTO, Offer.class);
    }

    private OfferDTO convertToOfferDTO(Offer offer) {
        return modelMapper.map(offer, OfferDTO.class);
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
