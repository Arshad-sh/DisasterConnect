package com.disasterconnect.security;

import com.disasterconnect.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // =========================================================
        // GET AUTHORIZATION HEADER
        // =========================================================

        String authHeader =
                request.getHeader("Authorization");

        // No Authorization header
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // =========================================================
        // EXTRACT JWT
        // =========================================================

        String jwtToken =
                authHeader.substring(7);

        // =========================================================
        // EXTRACT USERNAME / EMAIL
        // =========================================================

        String username;

        try {

            username =
                    jwtService.extractUsername(jwtToken);

        } catch (Exception exception) {

            // Invalid JWT
            filterChain.doFilter(request, response);
            return;
        }

        // =========================================================
        // CHECK SECURITY CONTEXT
        // =========================================================

        if (username != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            // =====================================================
            // LOAD USER FROM DATABASE
            // =====================================================

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            // =====================================================
            // VALIDATE JWT
            // =====================================================

            if (jwtService.isTokenValid(
                    jwtToken,
                    userDetails.getUsername())) {

                // =================================================
                // CREATE AUTHENTICATION
                // =================================================

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // =================================================
                // ADD REQUEST DETAILS
                // =================================================

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // =================================================
                // SET SECURITY CONTEXT
                // =================================================

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }
        }

        // =========================================================
        // CONTINUE FILTER CHAIN
        // =========================================================

        filterChain.doFilter(request, response);
    }
}