package ua.foxminded.warehouse.util.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.warehouse.models.auth.RegisteredUser;
import ua.foxminded.warehouse.security.RegisteredUserDetailsService;

@Component
public class RegisteredUserValidator implements Validator {
    private RegisteredUserDetailsService registeredUserDetailsService;

    @Autowired
    public RegisteredUserValidator(RegisteredUserDetailsService registeredUserDetailsService) {
        this.registeredUserDetailsService = registeredUserDetailsService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisteredUser.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisteredUser user = (RegisteredUser) target;
        try {
            registeredUserDetailsService.loadUserByUsername(user.getName());
        } catch (UsernameNotFoundException ignored) {
            return;
        }

        errors.rejectValue("name", "", "User with this username already exists");
    }
}
