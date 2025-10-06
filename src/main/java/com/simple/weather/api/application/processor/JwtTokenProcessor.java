package com.simple.weather.api.application.processor;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.simple.weather.api.application.model.ClientAuthentication;
import com.simple.weather.api.application.model.JwtTokenResponse;
import com.simple.weather.api.application.model.entity.ApiUser;
import com.simple.weather.api.application.service.ApiUserService;
import com.simple.weather.api.application.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProcessor
{
	private static final int EXPIRATION_HOUR = 24;
	
	private final JwtUtil jwtUtil;
	private final ApiUserService apiUserService;
	
	public JwtTokenResponse processToken(ClientAuthentication clientAuthentication)
	{
		JwtTokenResponse tokenResponse = null;
		if (clientAuthentication == null)
		{
			return tokenResponse;
		}
		
		String clientId = clientAuthentication.getClientId();
		ApiUser apiUser = apiUserService.fetchApiUser(clientId);
		if (apiUser == null)
		{
			return tokenResponse;
		}
		
		String token = jwtUtil.generateToken(clientId, apiUser.getClientSecret());
		long expiresIn = TimeUnit.HOURS.toSeconds(EXPIRATION_HOUR);
		
		tokenResponse = JwtTokenResponse.builder()
				.token(token)
				.expiresIn(expiresIn)
				.build();
		
		return tokenResponse;
	}
}
