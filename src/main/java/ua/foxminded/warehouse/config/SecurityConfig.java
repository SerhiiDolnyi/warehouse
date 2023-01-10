package ua.foxminded.warehouse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.foxminded.warehouse.security.RegisteredUserDetailsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private RegisteredUserDetailsService registeredUserDetailsService;

    @Autowired
    public SecurityConfig(RegisteredUserDetailsService registeredUserDetailsService) {
        this.registeredUserDetailsService = registeredUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // this mvcMatchers should be commented after have being tested rest API
                .mvcMatchers("/","/item", "/customer", "/supplier", "/invoice", "/offer", "/manager", "/item/**", "/customer/**", "/supplier/**", "/invoice/**", "/offer/**", "/manager/**", "/address", "/address/**")
                    .permitAll()

                /* // this code was commented to test Rest API
                .mvcMatchers("/admin").hasAnyAuthority("ADMIN")
                .mvcMatchers("/item", "/customer", "/supplier", "/invoice", "/offer", "/manager")
                    .hasAnyAuthority("ADMIN", "STAFF", "PARTNER", "USER")
                .mvcMatchers("/item/**", "/customer/**", "/supplier/**", "/invoice/**", "/offer/**", "/manager/**", "/address", "/address/**")
                    .hasAnyAuthority("ADMIN", "STAFF")
                .mvcMatchers("/", "/auth/login","/auth/registration", "error").permitAll()
                .anyRequest().authenticated()*/

                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/", true)
                .failureForwardUrl("/auth/login?error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(registeredUserDetailsService)
            .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(registeredUserDetailsService);
        authProvider.setPasswordEncoder(getPasswordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    /*Use this bean instead of above bean when Password should be encode
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

}
