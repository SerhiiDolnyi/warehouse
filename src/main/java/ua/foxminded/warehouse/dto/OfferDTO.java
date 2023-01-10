package ua.foxminded.warehouse.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import ua.foxminded.warehouse.models.Item;
import ua.foxminded.warehouse.models.Supplier;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OfferDTO {

    private int id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Date should be present or future")
    private Date date;

    @NotNull(message = "supplierId should not be empty")
    private int supplierId;

    @NotNull(message = "itemId should not be empty")
    private int itemId;

    @NotNull(message = "Price should not be empty")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price should be greater than 0")
    @DecimalMax(value = "100000.00", inclusive = false, message = "Price should be less than 10 000,00")
    @Digits(integer=6, fraction=2)
    private BigDecimal price;

    @NotNull(message = "Amount should not be empty")
    @Min(value = 1, message = "Amount should be greater than 0")
    @Max(value = 100, message = "Amount should be equal or less than 100")
    private int itemCount;

    @NotEmpty(message = "Stage should not be empty")
    private String stage;

    public OfferDTO() {
    }

    public OfferDTO(int id, Date date, int supplierId, int itemId, BigDecimal price, int itemCount, String stage) {
        this.id = id;
        this.date = date;
        this.supplierId = supplierId;
        this.itemId = itemId;
        this.price = price;
        this.itemCount = itemCount;
        this.stage = stage;
    }

    public OfferDTO(Date date, int supplierId, int itemId, BigDecimal price, int itemCount, String stage) {
        this.date = date;
        this.supplierId = supplierId;
        this.itemId = itemId;
        this.price = price;
        this.itemCount = itemCount;
        this.stage = stage;
    }
}
