package com.simple.weather.api.application.processor;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simple.weather.api.application.model.ClientAuthentication;
import com.simple.weather.api.application.model.JwtTokenResponse;
import com.simple.weather.api.application.model.entity.ApiUser;
import com.simple.weather.api.application.service.ApiUserService;
import com.simple.weather.api.application.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
class JwtTokenProcessorTest
{
	@Mock
	private ApiUserService apiUserService;
	@Mock
	private JwtUtil jwtUtil;
	@InjectMocks
	private JwtTokenProcessor processor;
	
	@Mock
	private ClientAuthentication clientAuthentication;
	
	@Test
	public void testProcessToken()
	{
		ApiUser apiUser = mock(ApiUser.class);
		
		when(clientAuthentication.getClientId()).thenReturn("testClientId");
		when(apiUserService.fetchApiUser(anyString())).thenReturn(apiUser);
		when(apiUser.getClientSecret()).thenReturn("testClientSecret");
		when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("testToken");
		
		JwtTokenResponse tokenResponse = processor.processToken(clientAuthentication);
		
		assertEquals("testToken", tokenResponse.getToken());
		assertEquals(86400l, tokenResponse.getExpiresIn());
	}
	
	@Test
	public void testProcessToken_InvalidClientAuthentication()
	{
		assertNull(processor.processToken(null));
	}
	
	@Test
	public void testProcessToken_ApiUserNotFound()
	{
		when(clientAuthentication.getClientId()).thenReturn("testClientId");
		when(apiUserService.fetchApiUser(anyString())).thenReturn(null);
		
		assertNull(processor.processToken(clientAuthentication));
	}
}
