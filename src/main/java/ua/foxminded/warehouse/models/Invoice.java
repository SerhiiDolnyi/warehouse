package ua.foxminded.warehouse.models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="invoice")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(includeFieldNames = true)
public class Invoice {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(name="date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Date should be present or future")
    private Date date;

    @ManyToOne
    @JoinColumn(name="customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="item_id", referencedColumnName = "id")
    private Item item;

    @Column(name="price")
    @NotNull(message = "Price should not be empty")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price should be greater than 0")
    @DecimalMax(value = "100000.00", inclusive = false, message = "Price should be less than 10 000,00")
    @Digits(integer=6, fraction=2)
    private BigDecimal price;

    @Column(name="item_count")
    @NotNull(message = "Amount should not be empty")
    @Min(value = 1, message = "Amount should be greater than 0")
    @Max(value = 100, message = "Amount should be equal or less than 100")
    private int itemCount;

    @Column(name="stage")
    private String stage;

    public Invoice(Date date, Customer customer, Item item, BigDecimal price, int itemCount, String stage) {
        this.date = date;
        this.customer = customer;
        this.item = item;
        this.price = price;
        this.itemCount = itemCount;
        this.stage = stage;
    }

    public Invoice(Date date, BigDecimal price, int itemCount, String stage) {
        this.date = date;
        this.price = price;
        this.itemCount = itemCount;
        this.stage = stage;
    }

    public Invoice(Date date) {
        this.date = date;
    }
}
