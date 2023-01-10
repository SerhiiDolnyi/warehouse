package ua.foxminded.warehouse.models;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name="item")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(includeFieldNames = false)
public class Item {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(name="name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 50 characters")
    private String name;

    @Column(name="category")
    private String category;

    @Column(name="description")
    @NotEmpty(message = "Description should not be empty")
    @Size(min = 2, max = 200, message = "Name should be between 2 and 100 characters")
    private String description;

    @Column(name="price")
    @NotNull(message = "Price should not be empty")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price should be greater than 0")
    @DecimalMax(value = "100000.00", inclusive = false, message = "Price should be less than 10 000,00")
    @Digits(integer=6, fraction=2)
    private BigDecimal price;

    @Column(name="amount")
    @NotNull(message = "Amount should not be empty")
    @Min(value = 1, message = "Amount should be greater than 0")
    @Max(value = 100, message = "Amount should be equal or less than 100")
    private int amount;

    @OneToMany(mappedBy = "item")
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "item")
    private List<Offer> offers;

    public Item(int id, String name, String category, String description, BigDecimal price, int amount) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    public Item(String name, String category, String description, BigDecimal price, int amount) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

}
