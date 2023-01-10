package ua.foxminded.warehouse.restcontrollers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.foxminded.warehouse.dto.AddressDTO;

@RestController
public class HomeConroller {
    @GetMapping("/")
    public ResponseEntity<String> initialString(){
        return new ResponseEntity<String>("Hello Test!", HttpStatus.OK);
    }
}
