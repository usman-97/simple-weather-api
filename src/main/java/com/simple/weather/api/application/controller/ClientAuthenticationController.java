package com.simple.weather.api.application.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.weather.api.application.model.ClientAuthentication;
import com.simple.weather.api.application.model.entity.ApiUser;
import com.simple.weather.api.application.service.ApiUserService;
import com.simple.weather.api.application.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class ClientAuthenticationController extends ControllerBase
{
	private final JwtUtil jwtUtil;
	private final ApiUserService apiUserService;
	
	@GetMapping("/token")
	public ResponseEntity<?> getToken(@RequestBody ClientAuthentication clientAuthentication)
	{
		String token = "";
		if (clientAuthentication != null)
		{
			String clientId = clientAuthentication.getClientId();
			ApiUser apiUser = apiUserService.fetchApiUser(clientId);
			if (apiUser != null)
			{
				token = jwtUtil.generateToken(clientId, apiUser.getClientSecret());
			}
		}
		
		return buildResponse(token);
	}
}
