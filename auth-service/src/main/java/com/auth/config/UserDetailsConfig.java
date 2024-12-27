package com.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserDetailsConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("user")
            .password("$2a$10$WzQvOBlIql.O0FawKTYtyu5uO4DK.w6TYDiK6tDhvUDoHnF94.6Ua") // Password: password (hashed)
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password("$2a$10$WzQvOBlIql.O0FawKTYtyu5uO4DK.w6TYDiK6tDhvUDoHnF94.6Ua") // Password: password (hashed)
            .roles("ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
