package com.example.socket.config;

import com.example.socket.jwt.JwtAccessDeniedHandler;
import com.example.socket.jwt.JwtAuthenticationEntryPoint;
import com.example.socket.jwt.JwtFilter;
import com.example.socket.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                // CORS 설정 추가
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/signup").permitAll()
                        .requestMatchers("/api/authenticate").permitAll()
                        .requestMatchers("/api/user").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/user/*").hasAnyRole("ADMIN")
                        .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS 설정을 위한 Bean 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 프론트엔드 주소
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}