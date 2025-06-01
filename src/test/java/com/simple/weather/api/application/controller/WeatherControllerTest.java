package com.simple.weather.api.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.simple.weather.api.application.processor.WeatherProcessor;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest
{
	@Mock
	private WeatherProcessor processor;
	@InjectMocks
	private WeatherController controller;
	
	@Test
	public void testProcessWeatherDetails()
	{
		when(processor.process(anyString())).thenReturn("{json}");
		
		ResponseEntity<?> response = controller.processWeatherDetails("keyword");
		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
	}
	
	@Test
	public void testProcessWeatherDetails_BadRequest()
	{
		when(processor.process(anyString())).thenReturn(null);
		
		ResponseEntity<?> response = controller.processWeatherDetails("keyword");
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
	}
}
