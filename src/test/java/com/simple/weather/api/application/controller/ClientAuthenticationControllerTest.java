package com.simple.weather.api.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.simple.weather.api.application.model.ClientAuthentication;
import com.simple.weather.api.application.model.entity.ApiUser;
import com.simple.weather.api.application.service.ApiUserService;
import com.simple.weather.api.application.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
class ClientAuthenticationControllerTest
{
	@Mock
	private JwtUtil jwtUtil;
	@Mock
	private ApiUserService apiUserService;
	@InjectMocks
	private ClientAuthenticationController clientAuthenticationController;
	
	@Mock
	private ClientAuthentication clientAuthentication;
	
	@Test
	@DisplayName("Should return a token when valid client authentication is provided")
	void shouldReturnTokenWhenValidClientAuthenticationIsProvided()
	{
		// Given
		String clientId = "testClientId";
		String clientSecret = "testClientSecret";
		String mockToken = "mock.jwt.token";
		
		ApiUser apiUser = new ApiUser();
		apiUser.setClientSecret(clientSecret);
		
		// Mock the dependencies' behavior
		when(clientAuthentication.getClientId()).thenReturn(clientId);
		when(apiUserService.fetchApiUser(clientId)).thenReturn(apiUser);
		when(jwtUtil.generateToken(clientId, clientSecret)).thenReturn(mockToken);
		
		// When
		ResponseEntity<?> responseEntity = clientAuthenticationController.getToken(clientAuthentication);
		
		// Then
		// Assuming buildResponse returns the token as the response body
		assertEquals(mockToken, responseEntity.getBody());
	}
	
	@Test
	@DisplayName("Should return empty token when client authentication is not found")
	void shouldReturnEmptyTokenWhenClientAuthenticationIsNotFound()
	{
		// Given
		String clientId = "unknownClientId";
		
		// Mock the dependencies' behavior
		when(clientAuthentication.getClientId()).thenReturn(clientId);
		when(apiUserService.fetchApiUser(clientId)).thenReturn(null);
		
		// When
		ResponseEntity<?> responseEntity = clientAuthenticationController.getToken(clientAuthentication);
		
		// Then
		// Assuming buildResponse returns an empty string for null/empty token
		assertEquals("", responseEntity.getBody());
	}
	
	@Test
	@DisplayName("Should return empty token when client authentication is null")
	void shouldReturnEmptyTokenWhenClientAuthenticationIsNull()
	{
		// Given
		ClientAuthentication clientAuthentication = null;
		
		// When
		ResponseEntity<?> responseEntity = clientAuthenticationController.getToken(clientAuthentication);
		
		// Then
		// Assuming buildResponse returns an empty string for a null input
		assertEquals("", responseEntity.getBody());
	}
}
