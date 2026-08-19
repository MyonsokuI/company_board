package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS設定を有効化し、下部で定義したcorsConfigurationSourceを適用
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // CSRF対策を無効化（APIサーバーのため）
                .csrf(csrf -> csrf.disable())
                // リクエストのアクセス権限設定
                .authorizeHttpRequests(auth -> auth
                        // /api/auth/** 以下のエンドポイントは認証なしでアクセス可能にする
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/notices/**").permitAll()
                        // その他のリクエストは認証を要求
                        .anyRequest().authenticated());

        return http.build();
    }

    // CORSの細かい許可ルールを定義するBean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // フロントエンドのオリジンを許可
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        // 許可するHTTPメソッド
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // すべてのヘッダーを許可
        configuration.setAllowedHeaders(List.of("*"));
        // 認証情報（Cookieや認可ヘッダーなど）の送信を許可する場合
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // すべてのエンドポイントに対してこのCORS設定を適用
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}