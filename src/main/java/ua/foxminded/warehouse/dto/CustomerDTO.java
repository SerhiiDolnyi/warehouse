package ua.foxminded.warehouse.dto;

import lombok.*;
import ua.foxminded.warehouse.models.Address;
import ua.foxminded.warehouse.models.Invoice;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CustomerDTO {

    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;

    @Min(value = 0, message = "Customer rate should be greater than 0")
    @Max(value = 10, message = "Customer rate should be equal or less than 10")
    private int customerRate;

    private int addressId;

    public CustomerDTO() {
    }

    public CustomerDTO(int id, String name, int customerRate, int addressId) {
        this.id = id;
        this.name = name;
        this.customerRate = customerRate;
        this.addressId = addressId;
    }

    public CustomerDTO(String name, int customerRate, int addressId) {
        this.name = name;
        this.customerRate = customerRate;
        this.addressId = addressId;
    }
}
