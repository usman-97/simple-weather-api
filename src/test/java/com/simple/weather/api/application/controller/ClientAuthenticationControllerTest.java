package com.simple.weather.api.application.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
	private ClientAuthenticationController controller;
	
	@Mock
	private ClientAuthentication clientAuthentication;
	@Mock
	private ApiUser apiUser;

	@Test
	public void testGetToken()
	{
		when(clientAuthentication.getClientId()).thenReturn("clientId");
		when(apiUserService.fetchApiUser(anyString())).thenReturn(apiUser);
		when(apiUser.getClientSecret()).thenReturn("secret");
		when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("token");
		
		ResponseEntity<?> response = controller.getToken(clientAuthentication);
		
		assertNotNull(response.getBody());
	}
	
	@Test
	public void testGetToken_NullClientAuthentication()
	{
		ResponseEntity<?> response = controller.getToken(null);
		
		assertNotNull(response.getBody());
	}
	
	@Test
	public void testGetToken_NullApiUser()
	{
		when(clientAuthentication.getClientId()).thenReturn("clientId");
		when(apiUserService.fetchApiUser(anyString())).thenReturn(null);
		
		ResponseEntity<?> response = controller.getToken(clientAuthentication);
		
		assertNotNull(response.getBody());
	}
}
