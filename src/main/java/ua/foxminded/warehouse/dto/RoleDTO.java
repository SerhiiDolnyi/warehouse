package ua.foxminded.warehouse.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Data
public class RoleDTO {

    private int id;

    @NotEmpty(message = "Role name should not be empty")
    private String name;
}
