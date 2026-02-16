package com.simple.weather.api.application.processor;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simple.weather.api.application.model.ClientAuthentication;
import com.simple.weather.api.application.model.JwtTokenResponse;
import com.simple.weather.api.application.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
class JwtTokenProcessorTest
{
	@Mock
	private JwtUtil jwtUtil;
	@InjectMocks
	private JwtTokenProcessor processor;
	
	@Mock
	private ClientAuthentication clientAuthentication;
	
	@Test
	public void testProcessToken()
	{
		when(clientAuthentication.getClientId()).thenReturn("testClientId");
		when(jwtUtil.generateToken(anyString(), any())).thenReturn("testToken");
		
		JwtTokenResponse tokenResponse = processor.processToken(clientAuthentication);
		
		assertEquals("testToken", tokenResponse.getToken());
		assertEquals(86400l, tokenResponse.getExpiresIn());
	}
	
	@Test
	public void testProcessToken_InvalidClientAuthentication()
	{
		assertNull(processor.processToken(null));
	}
}
