package ua.foxminded.warehouse.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ua.foxminded.warehouse.models.Address;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class SupplierDTO {

    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;

    @Min(value = 0, message = "Goods return rate should be greater than 0")
    @Max(value = 10, message = "Goods return rate should be equal or less than 10")
    private int goodsReturnRate;

    private int addressId;

    public SupplierDTO() {
    }

    public SupplierDTO(int id, String name, int goodsReturnRate, int addressId) {
        this.id = id;
        this.name = name;
        this.goodsReturnRate = goodsReturnRate;
        this.addressId = addressId;
    }

    public SupplierDTO(String name, int goodsReturnRate, int addressId) {
        this.name = name;
        this.goodsReturnRate = goodsReturnRate;
        this.addressId = addressId;
    }
}
