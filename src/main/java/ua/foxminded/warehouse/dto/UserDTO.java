package ua.foxminded.warehouse.dto;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class UserDTO {

    private int id;

    @NotEmpty(message = "User Name should not be empty")
    private String name;

    @NotEmpty(message = "User Name should not be empty")
    private String password;

    @NotEmpty(message = "User Role should not be empty")
    private String role;

    public UserDTO() {
    }

    public UserDTO(int id, String name, String password, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public UserDTO(String name, String password, String role) {
        this.name = name;
        this.password = password;
        this.role = role;
    }
}
