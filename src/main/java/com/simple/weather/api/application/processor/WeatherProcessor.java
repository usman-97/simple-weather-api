package com.simple.weather.api.application.processor;

import org.springframework.stereotype.Component;

import com.simple.weather.api.application.enums.WeatherApiMethod;
import com.simple.weather.api.application.handler.request.WeatherRequestHandler;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WeatherProcessor
{
	private final WeatherRequestHandler requestHandler;
	
	public String process(final String keyword)
	{
		return requestHandler.sendCurrentWeatherRequest(keyword, WeatherApiMethod.CURRENT);
	}
}
