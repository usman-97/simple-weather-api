package com.simple.weather.api.application.processor;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simple.weather.api.application.handler.request.WeatherRequestHandler;

@ExtendWith(MockitoExtension.class)
public class WeatherProcessorTest
{
	@Mock
	private WeatherRequestHandler requestHandler;
	@InjectMocks
	private WeatherProcessor processor;

	@Test
	public void testProcess()
	{
		when(requestHandler.sendCurrentWeatherRequest(anyString(), any())).thenReturn("{json}");
		
		assertNotNull(processor.process("keyword"));
	}
}
