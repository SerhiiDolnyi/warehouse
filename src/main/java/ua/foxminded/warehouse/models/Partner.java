package ua.foxminded.warehouse.models;


import lombok.*;

import javax.persistence.*;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Partner {

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String name;

    public Partner(String name) {
        this.name = name;
    }
}
