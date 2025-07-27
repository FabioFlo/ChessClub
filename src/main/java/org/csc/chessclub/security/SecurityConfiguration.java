package org.csc.chessclub.security;

import org.csc.chessclub.exception.CustomAuthenticationEntryPoint;
import org.csc.chessclub.exception.CustomRestAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final CustomRestAccessDeniedHandler customRestAccessDeniedHandler;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter,
      AuthenticationProvider authenticationProvider,
      CustomRestAccessDeniedHandler customRestAccessDeniedHandler,
      CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.authenticationProvider = authenticationProvider;
    this.customRestAccessDeniedHandler = customRestAccessDeniedHandler;
    this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    http.exceptionHandling(exceptionHandling ->
        exceptionHandling
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customRestAccessDeniedHandler));

    http.sessionManagement(sessionAuthenticationStrategy ->
        sessionAuthenticationStrategy.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.authenticationProvider(authenticationProvider);
    http.csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }


}
