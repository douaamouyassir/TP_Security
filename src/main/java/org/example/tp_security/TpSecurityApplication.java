package org.example.tp_security;

import org.example.tp_security.entities.Product;
import org.example.tp_security.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

// Si tu veux désactiver Spring Security temporairement :
// @SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@SpringBootApplication
public class TpSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(TpSecurityApplication.class, args);
    }

    @Bean
    public CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            productRepository.save(Product.builder()
                    .name("Computer")
                    .price(5400)
                    .quantity(12)
                    .build());
            productRepository.save(Product.builder()
                    .name("Printer")
                    .price(1200)
                    .quantity(11)
                    .build());
            productRepository.save(Product.builder()
                    .name("Smart Phone")
                    .price(12000)
                    .quantity(33)
                    .build());

            productRepository.findAll().forEach(System.out::println);
        };
    }
}
