package ua.foxminded.warehouse.dto;

import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class ItemDTO {

    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 50 characters")
    private String name;

    @NotEmpty(message = "Category should not be empty")
    private String category;

    @NotEmpty(message = "Description should not be empty")
    @Size(min = 2, max = 200, message = "Description should be between 2 and 100 characters")
    private String description;

    @NotNull(message = "Price should not be empty")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price should be greater than 0")
    @DecimalMax(value = "100000.00", inclusive = false, message = "Price should be less than 10 000,00")
    @Digits(integer=6, fraction=2)
    private BigDecimal price;

    @NotNull(message = "Amount should not be empty")
    @Min(value = 1, message = "Amount should be greater than 0")
    @Max(value = 100, message = "Amount should be equal or less than 100")
    private int amount;
}
