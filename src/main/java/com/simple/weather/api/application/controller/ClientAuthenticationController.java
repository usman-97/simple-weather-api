package com.simple.weather.api.application.controller;

import java.util.concurrent.TimeUnit;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.weather.api.application.model.ClientAuthentication;
import com.simple.weather.api.application.model.entity.ApiUser;
import com.simple.weather.api.application.service.ApiUserService;
import com.simple.weather.api.application.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
	public ResponseEntity<?> getToken(HttpServletResponse resp, @RequestBody ClientAuthentication clientAuthentication)
	{
		ResponseEntity<?> response = null;
		String token = generateToken(resp, clientAuthentication);
		if (!StringUtils.hasText(token))
		{
			response = buildResponse(false, "Error generating token", null, HttpStatus.SC_BAD_REQUEST);
		}
		else
		{
			response = buildResponse(true, "Successfully generated token", null, HttpStatus.SC_OK);
		}
		
		return response;
	}
	
	private String generateToken(HttpServletResponse resp, ClientAuthentication clientAuthentication)
	{
		String token = null;
		if (clientAuthentication == null)
		{
			return token;
		}
		
		String clientId = clientAuthentication.getClientId();
		ApiUser apiUser = apiUserService.fetchApiUser(clientId);
		if (apiUser == null)
		{
			return token;
		}
		
		token = jwtUtil.generateToken(clientId, apiUser.getClientSecret());
		long expiresIn = TimeUnit.HOURS.toSeconds(EXPIRATION_HOUR);
		
		Cookie cookie = new Cookie("jwt_auth", token);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge((int) expiresIn);
		cookie.setPath("/");
		resp.addCookie(cookie);
		
		return token;
	}
}
