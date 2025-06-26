package com.example.demo.security;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Resource(name = "userService")
    private UserDetailsService userDetailsService;

    private final JwtAuthEntryPoint unauthorizedHandler;

    public SecurityConfig(JwtAuthEntryPoint unauthorizedHandler) {
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Или укажи нужные домены
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public JwtAuthFilter authTokenFilterBean() {
        return new JwtAuthFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**")
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/generate-token", "/setup-admin", "/setup-teams", "/setup-daily-plan", "/setup-reports").permitAll()
                        .requestMatchers(HttpMethod.GET, "/check-setup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/employees").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/employees/{id}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/profiles/{id}/contract").hasAnyAuthority("ADMIN", "ADMIN_ACCOUNTANT", "ACCOUNTANT")
                        .requestMatchers("/reports", "/reports/{id}").hasAnyAuthority("ADMIN", "ADMIN_ACCOUNTANT", "ACCOUNTANT")
                        .requestMatchers("/current-report", "/current-report/recommended-recalculations").hasAnyAuthority("ADMIN", "ADMIN_ACCOUNTANT", "ACCOUNTANT")
                        .requestMatchers(HttpMethod.GET, "/employees/{managerId}/subordinates/holiday-requests").hasAnyAuthority("ADMIN", "ADMIN_ACCOUNTANT", "ADMIN_ANALYST", "ADMIN_WAREHOUSE")
                        .requestMatchers(HttpMethod.POST, "/employees/{managerId}/subordinates/{subordinateId}/holidays").hasAnyAuthority("ADMIN", "ADMIN_ACCOUNTANT", "ADMIN_ANALYST", "ADMIN_WAREHOUSE")
                        .requestMatchers("/deliveries", "/deliveries/{id}", "/deliveries/recommended-delivery").hasAnyAuthority("ADMIN", "ADMIN_WAREHOUSE", "WAREHOUSE")
                        .requestMatchers("/items", "/items/{id}", "/items/{id}/supply", "/items/{id}/buy").hasAnyAuthority("ADMIN", "ADMIN_WAREHOUSE", "WAREHOUSE")
                        .requestMatchers("/set-special-offer", "/cancel-special-offer").hasAuthority("ADMIN")
                        .requestMatchers("/daily-plan", "/scheduled-orders", "/special-plan").hasAnyAuthority("ADMIN", "ANALYST", "ADMIN_ANALYST")
                        .requestMatchers("/orders", "/orders/{id}").hasAnyAuthority("ADMIN", "ADMIN_WAREHOUSE", "WAREHOUSE")
                        .requestMatchers(HttpMethod.GET, "/suggestions", "/suggestions/{id}").hasAnyAuthority("ADMIN", "ACCOUNTANT", "ADMIN_ACCOUNTANT", "ANALYST", "ADMIN_ANALYST", "WAREHOUSE", "ADMIN_WAREHOUSE")
                        .requestMatchers("/employees/colleagues").hasAnyAuthority("ADMIN", "ACCOUNTANT", "ADMIN_ACCOUNTANT", "ANALYST", "ADMIN_ANALYST", "WAREHOUSE", "ADMIN_WAREHOUSE")
                        .requestMatchers(HttpMethod.POST, "/suggestions").hasAnyAuthority("ADMIN", "ACCOUNTANT", "ADMIN_ACCOUNTANT", "ANALYST", "ADMIN_ANALYST", "WAREHOUSE", "ADMIN_WAREHOUSE")
                        .requestMatchers(HttpMethod.PUT, "/suggestions/{id}").hasAnyAuthority("ADMIN", "ADMIN_ACCOUNTANT", "ADMIN_ANALYST", "ADMIN_WAREHOUSE")
                        .requestMatchers(HttpMethod.GET, "/tasks", "/tasks/{id}", "/kanban/{id}", "/indicators/{id}").hasAnyAuthority("ADMIN", "ACCOUNTANT", "ADMIN_ACCOUNTANT", "ANALYST", "ADMIN_ANALYST", "WAREHOUSE", "ADMIN_WAREHOUSE")
                        .requestMatchers(HttpMethod.POST, "/tasks").hasAnyAuthority("ADMIN", "ADMIN_ACCOUNTANT", "ADMIN_ANALYST", "ADMIN_WAREHOUSE")
                        .requestMatchers(HttpMethod.PUT, "/tasks/{id}", "/tasks/{id}/assign").hasAnyAuthority("ADMIN", "ACCOUNTANT", "ADMIN_ACCOUNTANT", "ANALYST", "ADMIN_ANALYST", "WAREHOUSE", "ADMIN_WAREHOUSE")
                        .requestMatchers("/notifications", "/notifications/{id}").hasAnyAuthority("ADMIN", "ACCOUNTANT", "ADMIN_ACCOUNTANT", "ANALYST", "ADMIN_ANALYST", "WAREHOUSE", "ADMIN_WAREHOUSE")
                        .requestMatchers("/assignment").hasAnyAuthority("ADMIN", "ADMIN_ACCOUNTANT", "ADMIN_ANALYST", "ADMIN_WAREHOUSE")
                        .requestMatchers("/app", "/chat", "/send/message").hasAnyAuthority("ADMIN", "ACCOUNTANT", "ADMIN_ACCOUNTANT", "ANALYST", "ADMIN_ANALYST", "WAREHOUSE", "ADMIN_WAREHOUSE")
                        .requestMatchers("/socket/**").permitAll()
                        .requestMatchers("/emails/*").hasAnyAuthority("ADMIN", "ADMIN_ACCOUNTANT", "ADMIN_ANALYST", "ADMIN_WAREHOUSE")
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .addFilterBefore(authTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

