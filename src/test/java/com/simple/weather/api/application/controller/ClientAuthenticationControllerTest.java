package com.simple.weather.api.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.simple.weather.api.application.model.ClientAuthentication;
import com.simple.weather.api.application.model.JwtTokenResponse;
import com.simple.weather.api.application.processor.JwtTokenProcessor;

@ExtendWith(MockitoExtension.class)
class ClientAuthenticationControllerTest
{
	@Mock
	private JwtTokenProcessor jwtTokenProcessor;
	@InjectMocks
	private ClientAuthenticationController controller;
	
	@Mock
	private ClientAuthentication clientAuthentication;

	@Test
	public void testGetToken()
	{
		when(jwtTokenProcessor.processToken(any())).thenReturn(mock(JwtTokenResponse.class));
		
		ResponseEntity<?> response = controller.getToken(clientAuthentication);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void testGetToken_NullClientAuthentication()
	{
		ResponseEntity<?> response = controller.getToken(null);
		
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
}
