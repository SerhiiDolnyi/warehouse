package ua.foxminded.warehouse.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.foxminded.warehouse.models.auth.RegisteredUser;
import ua.foxminded.warehouse.models.auth.Role;

import java.util.*;

public class RegisteredUserDetails implements UserDetails {
    private RegisteredUser registeredUser;

    public RegisteredUserDetails(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // If instead of "String role" will be used "Set<Role> roles" you should use this code:
        Set<Role> roles = registeredUser.getRoles();
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        /* should use if "String role" instead of "Set<Role> roles";
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(registeredUser.getRole()));*/

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.registeredUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.registeredUser.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
