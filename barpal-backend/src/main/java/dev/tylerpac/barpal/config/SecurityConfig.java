package dev.tylerpac.barpal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String FRONTEND_ORIGIN = System.getenv().getOrDefault("FRONTEND_ORIGIN", "http://192.168.1.26:3003");
    // Allow common local development variants; in production tighten to specific domain(s)
    private static final List<String> LOCAL_ALLOWED = List.of(
        FRONTEND_ORIGIN,
        "http://localhost:3003",
        "http://127.0.0.1:3003",
        "http://localhost",
        "http://127.0.0.1"
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Using stateless API; consider csrf token if session auth added
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(h -> h
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self'; object-src 'none'; frame-ancestors 'none'; base-uri 'self';"))
                        .xssProtection(x -> {})
                        .referrerPolicy(r -> r.policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                        .permissionsPolicy(p -> p.policy("geolocation=(), microphone=(), camera=()"))
                )
        .authorizeHttpRequests(auth -> auth
            // Basic infra & error endpoints
            .requestMatchers("/actuator/health", "/actuator/info", "/error").permitAll()
            // Allow CORS preflight globally
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            // TEMP: Open all status endpoints for troubleshooting (tighten later)
            .requestMatchers("/api/status/**").permitAll()
            .anyRequest().denyAll()
        );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(LOCAL_ALLOWED);
        config.setAllowedMethods(List.of("GET","POST","OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type","Authorization"));
        config.setAllowCredentials(false); // no cookies / auth yet
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        source.registerCorsConfiguration("/actuator/**", config);
        return source;
    }
}
