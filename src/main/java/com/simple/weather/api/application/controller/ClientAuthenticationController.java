package com.simple.weather.api.application.controller;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.weather.api.application.model.ClientAuthentication;
import com.simple.weather.api.application.model.JwtTokenResponse;
import com.simple.weather.api.application.processor.JwtTokenProcessor;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class ClientAuthenticationController extends ControllerBase
{
	private final JwtTokenProcessor jwtTokenProcessor;
	
	@PostMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getToken(@RequestBody ClientAuthentication clientAuthentication)
	{
		ResponseEntity<?> response = null;
		JwtTokenResponse tokenResponse = jwtTokenProcessor.processToken(clientAuthentication);
		if (tokenResponse == null)
		{
			response = buildResponse(false, "Error generating token", null, HttpStatus.SC_BAD_REQUEST);
		}
		else
		{
			response = buildResponse(true, "Successfully generated token", tokenResponse, HttpStatus.SC_OK);
		}
		
		return response;
	}
}
