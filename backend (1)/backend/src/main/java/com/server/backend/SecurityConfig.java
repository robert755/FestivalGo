package com.server.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Definirea unui encoder de parolă folosind BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()  // Permite accesul pentru toate cererile
                )
                .formLogin(form -> form
                        .loginPage("/register")  // Pagina de login personalizată
                        .permitAll()  // Permite accesul la pagina de login pentru toți utilizatorii
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL-ul pentru logout
                        .logoutSuccessUrl("/login?logout")  // Redirect după logout
                        .permitAll()
                );

        return http.build();
    }

}
