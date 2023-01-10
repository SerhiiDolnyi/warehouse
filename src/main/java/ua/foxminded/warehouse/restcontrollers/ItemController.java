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
import ua.foxminded.warehouse.dto.ItemDTO;
import ua.foxminded.warehouse.models.Item;
import ua.foxminded.warehouse.services.ItemService;
import ua.foxminded.warehouse.util.exception.address.AddressNotCreatedException;
import ua.foxminded.warehouse.util.exception.address.AddressNotFoundException;
import ua.foxminded.warehouse.util.exception.address.AddressNotUpdatedException;
import ua.foxminded.warehouse.util.exception.customer.CustomerNotCreatedException;
import ua.foxminded.warehouse.util.exception.item.ItemNotFoundException;
import ua.foxminded.warehouse.util.exception.item.ItemNotUpdatedException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Path.ITEM)
public class ItemController {
    private ItemService itemService;
    private ModelMapper modelMapper;
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    public ItemController(ItemService itemService, ModelMapper modelMapper) {
        this.itemService = itemService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get all Items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Items found successfully"),
            @ApiResponse(responseCode = "404", description = "Items not found")
    })
    @GetMapping(Path.GET_ALL_ITEM)
    public ResponseEntity<List<ItemDTO>> showAllItemDTO() {
        log.info("Searching all items ");
        List<ItemDTO> items = itemService.findAll().stream().map(this::convertToItemDTO)
                .collect(Collectors.toList());
        if(items.isEmpty()) {
            log.info("List of Items is empty...");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Items found successfully");

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @Operation(summary = "Get an Item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found Item"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @GetMapping(Path.GET_ITEM_BY_ID)
    public ResponseEntity<ItemDTO> getItemById(
            @Parameter(description = "ID of Item to be searched") @PathVariable("id") int id) {
        log.info("Searching item by ID");
        ItemDTO itemDTO;
        try {
            itemDTO = convertToItemDTO(itemService.findById(id));
        } catch (ItemNotFoundException e) {
            log.info("Item not found..." + e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("Item found successfully");

        return new ResponseEntity<>(itemDTO, HttpStatus.OK);
    }

    @Operation(summary = "Create a new Item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New Item added successfully"),
            @ApiResponse(responseCode = "400", description = "Request to create has error")
    })
    @PostMapping(Path.CREATE_ITEM)
    public ResponseEntity<String> createItem(@RequestBody @Valid ItemDTO itemDTO, BindingResult bindingResult) {
        log.info("Creating new item");
        if(bindingResult.hasErrors()) {
            String errorMsg = errorMessageBuilder(bindingResult);
            log.info("Error occure ..." + new CustomerNotCreatedException(errorMsg));
            return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        itemService.save(convertToItem(itemDTO));
        log.info("New item added successfully");

        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Update the item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Request to update has error"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @PutMapping(Path.UPDATE_ITEM)
    public ResponseEntity<String> update(@RequestBody @Valid ItemDTO itemDTO, BindingResult bindingResult,
                                         @Parameter(description = "ID of item to be updated") @PathVariable("id") int id) {
        log.info("Updating item with ID " + id);
        itemService.findById(id);
        if(bindingResult.hasErrors()) {
            String errorMsg = errorMessageBuilder(bindingResult);
            log.info("Error occure ..." + new ItemNotUpdatedException(errorMsg));
            return new ResponseEntity<String>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        itemService.update(id, convertToItem(itemDTO));
        log.info("Item updated successfully");

        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Delete Item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @DeleteMapping(Path.DELETE_ITEM_BY_ID)
    public ResponseEntity<String> delete(@Parameter(description = "ID of Item to be deleted") @PathVariable("id") int id) {
        log.info("Deleting address");
        try {
            itemService.findById(id);
        }   catch (ItemNotFoundException e) {
            log.info("Address not found..." + e);
            return new ResponseEntity<>("Item not found", HttpStatus.NOT_FOUND);
        }
        itemService.delete(id);
        log.info("Item deleted successfully");

        return new ResponseEntity(HttpStatus.OK);
    }

    private ItemDTO convertToItemDTO(Item item) {
        return modelMapper.map(item, ItemDTO.class);
    }

    private Item convertToItem(ItemDTO itemDTO) {
        return modelMapper.map(itemDTO, Item.class);
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
