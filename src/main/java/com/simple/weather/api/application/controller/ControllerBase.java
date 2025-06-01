package com.simple.weather.api.application.controller;

import org.springframework.http.ResponseEntity;

public abstract class ControllerBase
{
	public ResponseEntity<?> buildResponse(String response)
	{
		ResponseEntity<?> responseEntity = null;
		if (response == null)
		{
			responseEntity = ResponseEntity.badRequest().build();
		}
		else
		{
			responseEntity = ResponseEntity.ok(response);
		}
		return responseEntity;
	}
}
