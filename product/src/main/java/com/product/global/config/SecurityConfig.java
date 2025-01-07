package com.product.global.config;

import com.product.global.exception.ErrorCode;
import com.product.global.exception.ErrorResponse;
import com.product.global.filter.CustomErrorLogFilter;
import com.product.global.filter.JwtAuthenticationFilter;
import com.product.global.response.ApiResponse;
import com.product.global.util.JsonUtil;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JsonUtil jsonUtil;
    private final JwtAuthenticationFilter jwtFilter;
    private final CustomErrorLogFilter requestBodyCachingFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // --------------- 인증 정책 ---------------
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> {
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .addFilterBefore(requestBodyCachingFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

            // --------------- 인가 정책 ---------------
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                    .requestMatchers("/docs/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/products").hasRole("SELLER")
                    .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("SELLER")
                    .anyRequest().authenticated();
            })

            // --------------- 인증/인가 예외처리 ---------------
            .exceptionHandling(exception -> {
                exception.authenticationEntryPoint((req, res, e) -> {
                    /*
				        예외 발생시 스프링 컨테이너에서 바디 값을 읽어와
				        ContentCachingRequestWrapper 에 요청 바디 정보를 캐시할 수 있도록 합니다.
				     */
                    StreamUtils.copyToString(req.getInputStream(), StandardCharsets.UTF_8);
                    String responseBody = jsonUtil.toJsonString(ApiResponse.of(
                        HttpStatus.UNAUTHORIZED,
                        ErrorResponse.builder()
                            .errorCode(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getCode())
                            .errorMessage(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getMessage())
                            .build()));
                    res.setContentType("application/json;charset=UTF-8");
                    res.setStatus(HttpStatus.UNAUTHORIZED.value());
                    res.getWriter().write(responseBody);
                });

                exception.accessDeniedHandler((req, res, e) -> {

                    StreamUtils.copyToString(req.getInputStream(), StandardCharsets.UTF_8);
                    String responseBody = jsonUtil.toJsonString(ApiResponse.of(
                        HttpStatus.FORBIDDEN,
                        ErrorResponse.builder()
                            .errorCode(ErrorCode.ACCESS_DENIED_BY_SECURITY.getCode())
                            .errorMessage(ErrorCode.ACCESS_DENIED_BY_SECURITY.getMessage())
                            .build()));
                    res.setContentType("application/json;charset=UTF-8");
                    res.setStatus(HttpStatus.FORBIDDEN.value());
                    res.getWriter().write(responseBody);
                });
            })

            //--------------- csrf, cors 설정 ---------------
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}