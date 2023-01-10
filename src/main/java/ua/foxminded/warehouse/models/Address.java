package ua.foxminded.warehouse.models;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.*;
import org.hibernate.annotations.GeneratorType;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false, onlyExplicitlyIncluded = true)
@ToString
public class Address {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @Column(name="postcode")
    @Min(value = 1, message = "Postcode should be greater than 1")
    private int postcode;

    @Column(name="country")
    @NotEmpty(message = "Name should not be empty")
    private String country;

    @Column(name="city")
    @NotEmpty(message = "Name should not be empty")
    private String city;

    @Column(name="street")
    @NotEmpty(message = "Name should not be empty")
    private String street;

    @Column(name="office")
    @Min(value = 0, message = "Number should be greater than 0")
    private int office;

    @OneToOne(mappedBy = "address")
    private Customer customer;

    @OneToOne(mappedBy = "address")
    private Supplier supplier;

    public Address(int id, int postcode, String country, String city, String street, int office) {
        this.id = id;
        this.postcode = postcode;
        this.country = country;
        this.city = city;
        this.street = street;
        this.office = office;
    }

    public Address(int postcode, String country, String city, String street, int office) {
        this.postcode = postcode;
        this.country = country;
        this.city = city;
        this.street = street;
        this.office = office;
    }
}
