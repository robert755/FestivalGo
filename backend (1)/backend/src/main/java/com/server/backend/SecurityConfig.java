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
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/users/login", "/users/register").permitAll()  // Permite accesul la login și register
                        .anyRequest().authenticated()  // Restul necesită autentificare
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Pagină de login personalizată
                        .permitAll()  // Permite accesul la pagina de login
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL-ul pentru logout (poți schimba acest lucru)
                        .logoutSuccessUrl("/login?logout")  // Redirecționează utilizatorul după logout
                        .permitAll()  // Permite accesul la logout pentru toți utilizatorii
                );

        return http.build();
    }


}
