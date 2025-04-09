package com.server.backend;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173") // adresa frontendului tău
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // metode permise
                        .allowedHeaders("*") // toate headerele sunt acceptate
                        .allowCredentials(true); // dacă vrei să trimiți cookie-uri sau tokenuri
            }
        };
    }
}