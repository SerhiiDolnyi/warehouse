package ua.foxminded.warehouse.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.foxminded.warehouse.models.auth.RegisteredUser;
import ua.foxminded.warehouse.repositories.RegisteredUserRepository;
import ua.foxminded.warehouse.security.RegisteredUserDetails;

import java.util.Optional;

@Service
public class RegisteredUserDetailsService implements UserDetailsService {
    private final RegisteredUserRepository registeredUserRepository;

    @Autowired
    public RegisteredUserDetailsService(RegisteredUserRepository registeredUserRepository) {
        this.registeredUserRepository = registeredUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findByName(username);
        if(!registeredUser.isPresent())
            throw new UsernameNotFoundException("Could not find user!");

        return new RegisteredUserDetails(registeredUser.get());
    }
}
