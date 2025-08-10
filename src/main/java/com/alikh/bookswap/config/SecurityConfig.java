package com.alikh.bookswap.config;

import com.alikh.bookswap.filter.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // ───────────────────────────────────────────────────────────────
                // Authorisation rules
                // ───────────────────────────────────────────────────────────────
                .authorizeHttpRequests(auth -> auth
                        // ── public docs / health ───────────────────────────────
                        .requestMatchers(
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                                "/actuator/health").permitAll()

                        // ── auth endpoints ─────────────────────────────────────
                        .requestMatchers("/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/refresh").permitAll()
                        .requestMatchers("/api/auth/change-password", "/api/auth/logout").authenticated()

                        // ── book catalogue (read-only) ─────────────────────────
                        .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()

                        // ── book mutate operations – must be logged-in ─────────
                        .requestMatchers("/api/books/**").authenticated()

                        // ── “me” resources ─────────────────────────────────────
                        .requestMatchers("/api/me/**").authenticated()

                        // ── admin area ─────────────────────────────────────────
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // ── everything else is closed by default ───────────────
                        .anyRequest().denyAll()
                );

        return http.build();
    }

    // ───────────────────────────────────────────────────────────────
    // Beans
    // ───────────────────────────────────────────────────────────────
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
