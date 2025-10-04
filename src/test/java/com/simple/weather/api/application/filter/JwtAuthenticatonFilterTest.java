package com.simple.weather.api.application.filter;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import com.simple.weather.api.application.model.entity.ApiUser;
import com.simple.weather.api.application.service.ApiUserService;
import com.simple.weather.api.application.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticatonFilterTest
{
	private static final String VALID_TOKEN_STRING = "testToken";
	
	@InjectMocks
	private JwtAuthenticatonFilter jwtAuthenticatonFilter;
	@Mock
	private JwtUtil jwtUtil;
	@Mock
	private ApiUserService apiUserService;
	@Mock
	private HttpServletRequest req;
	@Mock
	private HttpServletResponse resp;
	@Mock
	private FilterChain filterChain;
	
	@BeforeEach
	void setUp()
	{
		// Clear the SecurityContextHolder before each test to ensure a clean state
		SecurityContextHolder.clearContext();
	}
	
	@Test
	@DisplayName("Should pass through for a public endpoint")
	void testDoFilterInternal_shouldPassThroughForPublicEndpoint() throws Exception
	{
		// Given
		when(req.getServletPath()).thenReturn("/v1/auth/login");
		
		// When
		jwtAuthenticatonFilter.doFilterInternal(req, resp, filterChain);
		
		// Then
		verify(filterChain, times(1)).doFilter(req, resp);
		verify(resp, never()).setStatus(anyInt());
		assertNull(SecurityContextHolder.getContext().getAuthentication());
	}
	
	@Test
	@DisplayName("Should return 401 for missing headers")
	void testDoFilterInternal_shouldReturn401ForMissingHeaders() throws Exception
	{
		// Given
		when(req.getServletPath()).thenReturn("/v1/protected/data");
		when(req.getHeader("X-Client-Id")).thenReturn(null);
		
		// When
		jwtAuthenticatonFilter.doFilterInternal(req, resp, filterChain);
		
		// Then
		verify(resp, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(filterChain, never()).doFilter(any(), any());
	}
	
	@Test
	@DisplayName("Should return 401 for malformed Authorization header")
	void testDoFilterInternal_shouldReturn401ForMalformedAuthorizationHeader() throws Exception
	{
		// Given
		when(req.getServletPath()).thenReturn("/v1/protected/data");
		when(req.getHeader("X-Client-Id")).thenReturn("test-client");
		when(req.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN_STRING);
		
		// When
		jwtAuthenticatonFilter.doFilterInternal(req, resp, filterChain);
		
		// Then
		verify(resp, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(filterChain, never()).doFilter(any(), any());
	}
	
	@Test
	@DisplayName("Should return 401 when ApiUser is not found")
	void testDoFilterInternal_shouldReturn401WhenApiUserIsNotFound() throws Exception
	{
		// Given
		String clientId = "test-client";
		
		when(req.getServletPath()).thenReturn("/v1/protected/data");
		when(req.getHeader("X-Client-Id")).thenReturn(clientId);
		when(apiUserService.fetchApiUser(clientId)).thenReturn(null);
		when(req.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN_STRING);
		
		// When
		jwtAuthenticatonFilter.doFilterInternal(req, resp, filterChain);
		
		// Then
		verify(resp, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(filterChain, never()).doFilter(any(), any());
	}
	
	@Test
	@DisplayName("Should return 401 when token validation fails")
	void testDoFilterInternal_shouldReturn401WhenTokenValidationFails() throws Exception
	{
		// Given
		String clientId = "test-client";
		String clientSecret = "test-secret";
		ApiUser apiUser = new ApiUser();
		
		apiUser.setClientSecret(clientSecret);
		
		when(req.getServletPath()).thenReturn("/v1/protected/data");
		when(req.getHeader("X-Client-Id")).thenReturn(clientId);
		when(apiUserService.fetchApiUser(clientId)).thenReturn(apiUser);
		when(jwtUtil.validateToken(anyString(), eq(clientSecret))).thenReturn(null);
		when(req.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN_STRING);
		
		// When
		jwtAuthenticatonFilter.doFilterInternal(req, resp, filterChain);
		
		// Then
		verify(resp, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(filterChain, never()).doFilter(any(), any());
	}
	
	@Test
	@DisplayName("Should successfully authenticate a valid token and proceed")
	void testDoFilterInternal_shouldSuccessfullyAuthenticateAndProceed() throws Exception
	{
		// Given
		String clientId = "test-client";
		String clientSecret = "test-secret";
		ApiUser apiUser = new ApiUser();
		
		apiUser.setClientSecret(clientSecret);
		
		Claims claims = Jwts.claims().subject(clientId).build();
		
		when(req.getServletPath()).thenReturn("/v1/protected/data");
		when(req.getHeader("X-Client-Id")).thenReturn(clientId);
		when(req.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN_STRING);
		when(apiUserService.fetchApiUser(clientId)).thenReturn(apiUser);
		when(jwtUtil.validateToken(VALID_TOKEN_STRING, clientSecret)).thenReturn(claims);
		
		// When
		jwtAuthenticatonFilter.doFilterInternal(req, resp, filterChain);
		
		// Then
		verify(filterChain, times(1)).doFilter(req, resp);
		verify(resp, never()).setStatus(anyInt());
		assertNotNull(SecurityContextHolder.getContext().getAuthentication());
	}
	
	@Test
	void testDoFilterInternal_NoTokenFound() throws Exception
	{
		// Given
		String clientId = "test-client";
		String clientSecret = "test-secret";
		ApiUser apiUser = new ApiUser();
		
		apiUser.setClientSecret(clientSecret);
		
		when(req.getServletPath()).thenReturn("/v1/protected/data");
		when(req.getHeader("X-Client-Id")).thenReturn(clientId);
		when(req.getHeader("Authorization")).thenReturn("Bearer ");
		
		// When
		jwtAuthenticatonFilter.doFilterInternal(req, resp, filterChain);
		
		// Then
		verify(resp, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(filterChain, never()).doFilter(any(), any());
	}
	
	@Test
	void testDoFilterInternal_InvalidAuthHeader() throws Exception
	{
		// Given
		String clientId = "test-client";
		String clientSecret = "test-secret";
		ApiUser apiUser = new ApiUser();
		
		apiUser.setClientSecret(clientSecret);
		
		when(req.getServletPath()).thenReturn("/v1/protected/data");
		when(req.getHeader("X-Client-Id")).thenReturn(clientId);
		when(req.getHeader("Authorization")).thenReturn("Error");
		
		// When
		jwtAuthenticatonFilter.doFilterInternal(req, resp, filterChain);
		
		// Then
		verify(resp, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		verify(filterChain, never()).doFilter(any(), any());
	}
}
