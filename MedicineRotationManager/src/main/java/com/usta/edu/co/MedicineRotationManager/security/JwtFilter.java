package com.usta.edu.co.MedicineRotationManager.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.usta.edu.co.MedicineRotationManager.services.CustomUserDetailsService;
import com.usta.edu.co.MedicineRotationManager.services.JwtService;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

        private final JwtService jwtService;
        private final CustomUserDetailsService userDetailsService;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                        FilterChain filterChain) throws ServletException, IOException {

                final String authHeader = request.getHeader("Authorization");

                if (authHeader == null
                                || !authHeader.startsWith("Bearer ")) {

                        filterChain.doFilter(request, response);

                        return;
                }

                final String token = authHeader.substring(7);

                try {
                        final String dni = jwtService.extractUsername(token);

                        if (dni != null
                                        && SecurityContextHolder
                                                        .getContext()
                                                        .getAuthentication() == null) {

                                UserDetails user = userDetailsService.loadUserByUsername(dni);

                                if (jwtService.isTokenValid(token, user)) {

                                        UsernamePasswordAuthenticationToken auth =

                                                        new UsernamePasswordAuthenticationToken(

                                                                        user,
                                                                        null,
                                                                        user.getAuthorities());

                                        auth.setDetails(user);

                                        SecurityContextHolder

                                                        .getContext()

                                                        .setAuthentication(auth);
                                }
                        }

                } catch (Exception e) {

                        System.out.println(
                                        "JWT inválido: "
                                                        + e.getMessage());
                }

                filterChain.doFilter(request, response);
        }
}
