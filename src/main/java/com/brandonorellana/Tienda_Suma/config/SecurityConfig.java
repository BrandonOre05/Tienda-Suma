package com.brandonorellana.Tienda_Suma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas
                        .requestMatchers("/", "/login", "/registro", "/registro/**", "/css/**", "/js/**", "/img/**").permitAll()

                        // Rutas solo ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Rutas para ADMIN y VENDEDOR
                        .requestMatchers("/productos/nuevo", "/productos/guardar", "/productos/editar/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers("/ventas/nueva", "/ventas/guardar").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers("/ventas").hasAnyRole("ADMIN", "VENDEDOR")

                        // Rutas para CLIENTE y ADMIN
                        .requestMatchers("/compras/**").hasAnyRole("CLIENTE", "ADMIN")

                        // Rutas para todos los autenticados
                        .requestMatchers("/home", "/productos", "/productos/detalle/**", "/ventas/detalle/**", "/perfil/**").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}