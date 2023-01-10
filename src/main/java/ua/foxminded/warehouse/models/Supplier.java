package ua.foxminded.warehouse.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Table(name="supplier")
@AttributeOverride(name="name", column=@Column(name = "name"))
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(callSuper = true, includeFieldNames = true)
public class Supplier extends Partner {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name="goods_return_rate")
    @Min(value = 0, message = "Goods return rate should be greater than 0")
    @Max(value = 10, message = "Goods return rate should be equal or less than 10")
    private int goodsReturnRate;

    @OneToMany(mappedBy = "supplier")
    private List<Offer> offers;

    public Supplier(String name, int goodsReturnRate) {
        super(name);
        this.goodsReturnRate = goodsReturnRate;
    }
}
