package edu.co.ustavillavo.quizaguilaravila.config;

import edu.co.ustavillavo.quizaguilaravila.model.AppUser;
import edu.co.ustavillavo.quizaguilaravila.model.Role;
import edu.co.ustavillavo.quizaguilaravila.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/trucks/**").hasAnyRole("ADMIN", "DRIVER")
                        .requestMatchers(HttpMethod.POST, "/api/trucks/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/trucks/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/trucks/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(AppUserRepository userRepository) {
        return username -> {
            AppUser user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            return new User(
                    user.getUsername(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );
        };
    }

    @Bean
    public CommandLineRunner initUsers(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                AppUser admin = new AppUser();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
            }

            if (userRepository.findByUsername("driver1").isEmpty()) {
                AppUser driver = new AppUser();
                driver.setUsername("driver1");
                driver.setPassword(passwordEncoder.encode("driver123"));
                driver.setRole(Role.DRIVER);
                userRepository.save(driver);
            }
        };
    }
}