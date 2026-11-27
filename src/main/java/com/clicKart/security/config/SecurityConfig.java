package com.clicKart.security.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;
import com.clicKart.security.filter.JwtAuthenticationFilter;
import com.clicKart.security.handler.CustomAccessDeniedHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .cors(Customizer.withDefaults())

            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

            	    // Public endpoints
            	    .requestMatchers(
            	        "/api/users",
            	        "/api/auth/login"
            	    ).permitAll()

            	    // Admin-only endpoints
            	    .requestMatchers("/api/admin/**").hasRole("ADMIN")

            	    // Product write operations - Admin only
            	    .requestMatchers(
            	        HttpMethod.POST, "/api/products", "/api/products/**"
            	    ).hasRole("ADMIN")

            	    .requestMatchers(
            	        HttpMethod.PUT, "/api/products", "/api/products/**"
            	    ).hasRole("ADMIN")

            	    .requestMatchers(
            	        HttpMethod.PATCH, "/api/products", "/api/products/**"
            	    ).hasRole("ADMIN")

            	    .requestMatchers(
            	        HttpMethod.DELETE, "/api/products", "/api/products/**"
            	    ).hasRole("ADMIN")

            	    // Category write operations - Admin only
            	    .requestMatchers(
            	        HttpMethod.POST, "/api/categories", "/api/categories/**"
            	    ).hasRole("ADMIN")

            	    .requestMatchers(
            	        HttpMethod.PUT, "/api/categories", "/api/categories/**"
            	    ).hasRole("ADMIN")

            	    .requestMatchers(
            	        HttpMethod.PATCH, "/api/categories", "/api/categories/**"
            	    ).hasRole("ADMIN")

            	    .requestMatchers(
            	        HttpMethod.DELETE, "/api/categories", "/api/categories/**"
            	    ).hasRole("ADMIN")

            	    // Product and category GET requests are public
            	    .requestMatchers(
            	        HttpMethod.GET,
            	        "/api/products", "/api/products/**",
            	        "/api/categories", "/api/categories/**"
            	    ).permitAll()

            	    // Both customers and admins can access their own profile
            	    .requestMatchers("/api/users/me/**")
            	    .hasAnyRole("USER", "ADMIN")

            	    // Customer-only endpoints
            	    .requestMatchers(
            	        "/api/cart/**",
            	        "/api/orders/**"
            	    ).hasRole("USER")

            	    .anyRequest().authenticated()
            	)

            .exceptionHandling(exception ->
                exception.accessDeniedHandler(
                    customAccessDeniedHandler
                )
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }


   
@Bean
public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration configuration =
        new CorsConfiguration();

    configuration.setAllowedOrigins(
        List.of(
            "http://localhost:5173",
            "https://clic-kart-frontend.vercel.app"
        )
    );

    configuration.setAllowedMethods(
        List.of(
            "GET",
            "POST",
            "PUT",
            "PATCH",
            "DELETE",
            "OPTIONS"
        )
    );

    configuration.setAllowedHeaders(
        List.of("*")
    );

    configuration.setAllowCredentials(true);

    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source =
        new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration(
        "/**",
        configuration
    );

    return source;
}

}
