package com.example.pems.config;
import com.example.pems.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // REST API ke liye CSRF disabled
                .csrf(csrf -> csrf.disable())

                // CorsConfig.java ki CORS configuration use karega
                .cors(cors -> {})

                // JWT based stateless authentication
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // CORS preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Swagger / OpenAPI
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api-docs/**"
                        ).permitAll()

                        // Login
                        .requestMatchers("/api/auth/**").permitAll()

                        // Register
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                        // Admin-only management APIs
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")

                        // Shared category catalog is read-only for users
                        .requestMatchers(HttpMethod.POST, "/api/categories/**", "/api/income-categories/**").denyAll()
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**", "/api/income-categories/**").denyAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**", "/api/income-categories/**").denyAll()

                        // Baaki sab JWT ke saath
                        .anyRequest().authenticated()
                )

                // Authentication fail hone par 401
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(
                                new HttpStatusEntryPoint(
                                        HttpStatus.UNAUTHORIZED
                                )
                        )
                )

                // JWT filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
