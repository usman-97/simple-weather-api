package com.simple.weather.api.application.handler.request;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simple.weather.api.application.client.WeatherClient;
import com.simple.weather.api.application.enums.HttpMethod;
import com.simple.weather.api.application.enums.WeatherApiMethod;
import com.simple.weather.api.application.model.CurrentWeatherData;
import com.simple.weather.api.application.model.WeatherData;
import com.simple.weather.api.application.properties.WeatherProperties;
import com.simple.weather.api.application.util.JsonUtil;

@ExtendWith(MockitoExtension.class)
public class WeatherRequestHandlerTest
{
	@Mock
	private WeatherClient weatherClient;
	@Mock
	private WeatherProperties properties;
	@Mock
	private JsonUtil jsonUtil;
	@InjectMocks
	private WeatherRequestHandler requestHandler;
	
	@Test
	public void testSendCurrentWeatherRequest()
	{
		WeatherData weatherData = mock(WeatherData.class);
		CurrentWeatherData current = mock(CurrentWeatherData.class);
		
		when(properties.getUrl()).thenReturn("url/");
		when(properties.getKey()).thenReturn("key");
		when(weatherClient.sendCurrentWeatherRequest(eq(HttpMethod.GET), anyString())).thenReturn("response");
		when(jsonUtil.jsonToObject(anyString(), eq(WeatherData.class))).thenReturn(weatherData);
		when(weatherData.getWeatherData()).thenReturn(current);
		when(jsonUtil.ObjectToJson(any())).thenReturn("{json}");
		
		assertNotNull(requestHandler.sendCurrentWeatherRequest("keyword", WeatherApiMethod.CURRENT));
	}

	@Test
	public void testSendCurrentWeatherRequest_NullResponse()
	{
		when(properties.getUrl()).thenReturn("url/");
		when(properties.getKey()).thenReturn("key");
		when(weatherClient.sendCurrentWeatherRequest(eq(HttpMethod.GET), anyString())).thenReturn(null);
		
		assertNull(requestHandler.sendCurrentWeatherRequest("keyword", WeatherApiMethod.CURRENT));
		
		verifyNoInteractions(jsonUtil);
	}
	
	@Test
	public void testSendCurrentWeatherRequest_NullWeatherDataInResponse()
	{
		when(properties.getUrl()).thenReturn("url/");
		when(properties.getKey()).thenReturn("key");
		when(weatherClient.sendCurrentWeatherRequest(eq(HttpMethod.GET), anyString())).thenReturn("response");
		when(jsonUtil.jsonToObject(anyString(), eq(WeatherData.class))).thenReturn(null);
		
		assertNull(requestHandler.sendCurrentWeatherRequest("keyword", WeatherApiMethod.CURRENT));
	}
	
	@Test
	public void testSendCurrentWeatherRequest_NullCurrentWeatherDataInResponse()
	{
		WeatherData weatherData = mock(WeatherData.class);
		
		when(properties.getUrl()).thenReturn("url/");
		when(properties.getKey()).thenReturn("key");
		when(weatherClient.sendCurrentWeatherRequest(eq(HttpMethod.GET), anyString())).thenReturn("response");
		when(jsonUtil.jsonToObject(anyString(), eq(WeatherData.class))).thenReturn(weatherData);
		when(weatherData.getWeatherData()).thenReturn(null);
		
		assertNull(requestHandler.sendCurrentWeatherRequest("keyword", WeatherApiMethod.CURRENT));
	}
}
