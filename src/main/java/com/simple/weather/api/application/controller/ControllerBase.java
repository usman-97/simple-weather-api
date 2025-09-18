package com.simple.weather.api.application.controller;

import org.springframework.http.ResponseEntity;

import com.simple.weather.api.application.model.request.ApiResponse;

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
	
	public <T> ResponseEntity<?> buildResponse(boolean success, String message, T data)
	{
		ApiResponse<T> response = ApiResponse.<T>builder()
				.success(success)
				.message(message)
				.data(data)
				.build();
		
		return  ResponseEntity.ok(response);
	}
}
