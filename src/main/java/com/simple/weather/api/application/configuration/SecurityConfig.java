package com.simple.weather.api.application.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.simple.weather.api.application.filter.JwtAuthenticatonFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
	private final JwtAuthenticatonFilter jwtAuthenticatonFilter;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		return http
				.cors(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.
						requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/v1/auth/**").permitAll()
						.anyRequest()
						.authenticated())
				.addFilterBefore(jwtAuthenticatonFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		return request -> {
			String origin = request.getHeader("Origin");
			
			CorsConfiguration config = new CorsConfiguration();
			
			List<String> allowedOrigins = Arrays.asList(
				"http://localhost:5000"
			);

			if (allowedOrigins.contains(origin)) {
				config.setAllowedOrigins(Arrays.asList(origin)); 
			}
			else
			{
				config.setAllowedOrigins(Collections.emptyList()); 
			}

			config.setAllowCredentials(true);
			config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "X-Client-Id"));
			config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			config.setMaxAge(7200L);
			
			return config;
		};
	}
}
