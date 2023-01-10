package ua.foxminded.warehouse.models;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Entity
@Table(name="customer")
@AttributeOverride(name="name", column=@Column(name = "name"))
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Customer extends Partner{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(name="customer_rate")
    @Min(value = 0, message = "Customer rate should be greater than 0")
    @Max(value = 10, message = "Customer rate should be equal or less than 10")
    private int customerRate;

    @OneToMany(mappedBy = "customer")
    private List<Invoice> invoices;

    public Customer(int id, String name, Address address, int customerRate) {
        super(name);
        this.id = id;
        this.address = address;
        this.customerRate = customerRate;
    }

    public Customer(String name, int customerRate) {
        super(name);
        this.customerRate = customerRate;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", customerRate=" + customerRate +
                '}' + super.toString();
    }
}
