package com.simple.weather.api.application.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.weather.api.application.model.ClientAuthentication;
import com.simple.weather.api.application.model.JwtTokenResponse;
import com.simple.weather.api.application.model.entity.ApiUser;
import com.simple.weather.api.application.service.ApiUserService;
import com.simple.weather.api.application.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class ClientAuthenticationController extends ControllerBase
{
	private static final int EXPIRATION_HOUR = 24;
	
	private final JwtUtil jwtUtil;
	private final ApiUserService apiUserService;
	
	@GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getToken(@RequestBody ClientAuthentication clientAuthentication)
	{
		JwtTokenResponse tokenResponse = null;
		String message = "Error generating token";
		if (clientAuthentication != null)
		{
			String clientId = clientAuthentication.getClientId();
			ApiUser apiUser = apiUserService.fetchApiUser(clientId);
			if (apiUser != null)
			{
				String token = jwtUtil.generateToken(clientId, apiUser.getClientSecret());
				long expiresIn = TimeUnit.HOURS.toSeconds(EXPIRATION_HOUR);
				tokenResponse = JwtTokenResponse.builder()
						.token(token)
						.expiresIn(expiresIn)
						.build();
				message = "Token generated successfully";
			}
		}
		
		return buildResponse(tokenResponse != null, message, tokenResponse);
	}
}
