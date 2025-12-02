package org.example.tp_security.sec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // ===============================
    // Password encoder
    // ===============================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ===============================
    // Users en mémoire
    // ===============================
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder().encode("1234")).roles("USER").build(),
                User.withUsername("user2").password(passwordEncoder().encode("1234")).roles("USER").build(),
                User.withUsername("admin").password(passwordEncoder().encode("1234")).roles("USER", "ADMIN").build()
        );
    }

    // ===============================
    // Configuration Spring Security
    // ===============================
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // ----- Page Login -----
                .formLogin(fl -> fl
                        .loginPage("/login")
                        .defaultSuccessUrl("/user/index", true)
                        .permitAll()
                )

                // ----- Logout -----
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                )

                // ----- Désactivation CSRF pour H2 + tests -----
                .csrf(csrf -> csrf.disable())

                // ----- Autorisations -----
                .authorizeHttpRequests(ar -> ar
                        .requestMatchers("/login", "/error").permitAll()      // IMPORTANT → évite les redirections infinies
                        .requestMatchers("/public/**", "/webjars/**").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // ----- Page si accès refusé -----
                .exceptionHandling(eh -> eh.accessDeniedPage("/notAuthorized"))

                .build();
    }
}
