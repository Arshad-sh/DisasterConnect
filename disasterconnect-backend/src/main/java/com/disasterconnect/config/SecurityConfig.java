package com.disasterconnect.config;

import com.disasterconnect.security.JwtAuthenticationFilter;
import com.disasterconnect.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    // =========================================================
    // SECURITY FILTER CHAIN
    // =========================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {

        http
                // =================================================
                // CSRF
                // =================================================
                .csrf(csrf -> csrf.disable())

                // =================================================
                // CORS
                // =================================================
                .cors(cors -> {})

                // =================================================
                // SESSION MANAGEMENT
                // =================================================
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // =================================================
                // AUTHORIZATION / RBAC
                // =================================================
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/health", "/api/auth/**")
                        .permitAll()

                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/citizen/**")
                        .hasRole("CITIZEN")

                        .requestMatchers("/api/ngo/**")
                        .hasRole("NGO")

                        .requestMatchers("/api/volunteer/**")
                        .hasRole("VOLUNTEER")

                        // Volunteer assignment reads/status updates.
                        .requestMatchers(HttpMethod.GET, "/api/assignments/my")
                        .hasRole("VOLUNTEER")

                        .requestMatchers(HttpMethod.GET, "/api/assignments/*")
                        .hasRole("VOLUNTEER")

                        .requestMatchers(HttpMethod.PUT, "/api/assignments/*/status")
                        .hasRole("VOLUNTEER")

                        // Assignment creation is an admin operation.
                        .requestMatchers(HttpMethod.POST, "/api/assignments")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/users/**")
                        .hasRole("ADMIN")

                        // Citizen requests: citizens create/manage their own;
                        // admins and NGOs can read the request feed.
                        .requestMatchers(HttpMethod.POST, "/api/requests")
                        .hasRole("CITIZEN")

                        .requestMatchers("/api/requests/my")
                        .hasRole("CITIZEN")

                        .requestMatchers(HttpMethod.PUT, "/api/requests/*/status")
                        .hasRole("CITIZEN")

                        .requestMatchers(HttpMethod.PUT, "/api/requests/*")
                        .hasRole("CITIZEN")

                        .requestMatchers(HttpMethod.DELETE, "/api/requests/*")
                        .hasRole("CITIZEN")

                        .requestMatchers(HttpMethod.GET, "/api/requests")
                        .hasAnyRole("ADMIN", "NGO", "CITIZEN")

                        .requestMatchers(HttpMethod.GET, "/api/requests/*")
                        .hasAnyRole("ADMIN", "NGO", "CITIZEN")

                        .anyRequest()
                        .authenticated()
                )

                // =================================================
                // JWT FILTER
                // =================================================
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }


    // =========================================================
    // CORS CONFIGURATION
    // =========================================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        "https://disasterconnect-sooty.vercel.app"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }


    // =========================================================
    // PASSWORD ENCODER
    // =========================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // =========================================================
    // AUTHENTICATION PROVIDER
    // =========================================================

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }


    // =========================================================
    // AUTHENTICATION MANAGER
    // =========================================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}