package ua.foxminded.warehouse.dto;


import lombok.*;
import ua.foxminded.warehouse.models.Customer;
import ua.foxminded.warehouse.models.Supplier;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class AddressDTO {

    private int id;

    @Min(value = 1, message = "Postcode should be greater than 1")
    private int postcode;

    @NotEmpty(message = "Name should not be empty")
    private String country;

    @NotEmpty(message = "Name should not be empty")
    private String city;

    @NotEmpty(message = "Name should not be empty")
    private String street;

    @Min(value = 0, message = "Number should be greater than 0")
    private int office;

    public AddressDTO(int postcode, String country, String city, String street, int office) {
        this.postcode = postcode;
        this.country = country;
        this.city = city;
        this.street = street;
        this.office = office;
    }
}
