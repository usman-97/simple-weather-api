package com.simple.weather.api.application.handler.request;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.type.TypeReference;
import com.simple.weather.api.application.client.SearchAutoCompleteClient;
import com.simple.weather.api.application.client.WeatherClient;
import com.simple.weather.api.application.enums.HttpMethod;
import com.simple.weather.api.application.enums.WeatherApiMethod;
import com.simple.weather.api.application.model.CurrentWeatherData;
import com.simple.weather.api.application.model.Location;
import com.simple.weather.api.application.model.WeatherData;
import com.simple.weather.api.application.properties.WeatherProperties;
import com.simple.weather.api.application.util.JsonUtil;

@ExtendWith(MockitoExtension.class)
public class WeatherRequestHandlerTest
{
	private static final String TEST_KEYWORD = "lon";
	private static final String API_KEY = "test-api-key";
	private static final String BASE_URL = "https://api.weather.com/";
	private static final String API_METHOD_VALUE = "search";
	private static final String RAW_RESPONSE = "[{\"name\":\"London\"},{\"name\":\"Londonderry\"}]";
	private static final String PROCESSED_RESPONSE = "[{\"name\":\"London\"},{\"name\":\"Londonderry\"}]";
	private static final List<Location> MOCK_LOCATIONS = List.of(
			new Location(),
			new Location()
		);
	
	@Mock
	private WeatherClient weatherClient;
	@Mock
	private WeatherProperties properties;
	@Mock
	private JsonUtil jsonUtil;
	@Mock
	private SearchAutoCompleteClient searchAutoCompleteClient;
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
	
	@SuppressWarnings("unchecked")
	@Test
	void testSendSearchAutoCompleteRequest_validRequest_returnsProcessedResponse() 
	{
		WeatherApiMethod apiMethod = WeatherApiMethod.SEARCH;
		
		when(apiMethod.getValue()).thenReturn(API_METHOD_VALUE);
		when(searchAutoCompleteClient.sendSearchLocationsRequest(any(), anyString())).thenReturn(RAW_RESPONSE);
		when(jsonUtil.jsonToObject(anyString(), any(TypeReference.class))).thenReturn(MOCK_LOCATIONS);
		when(jsonUtil.ObjectToJson(MOCK_LOCATIONS)).thenReturn(PROCESSED_RESPONSE);

		String result = requestHandler.sendSearchAutoCompleteRequest(TEST_KEYWORD, apiMethod);

		assertEquals(PROCESSED_RESPONSE, result);
		verify(searchAutoCompleteClient).sendSearchLocationsRequest(any(), any());
		verify(jsonUtil).jsonToObject(anyString(), any(TypeReference.class));
		verify(jsonUtil).ObjectToJson(MOCK_LOCATIONS);
	}

	@SuppressWarnings("unchecked")
	@Test
	void testSendSearchAutoCompleteRequest_nullResponse_returnsNull() 
	{
		when(searchAutoCompleteClient.sendSearchLocationsRequest(any(), anyString())).thenReturn(null);

		String result = requestHandler.sendSearchAutoCompleteRequest(TEST_KEYWORD, WeatherApiMethod.SEARCH);

		assertNull(result);
		verify(jsonUtil, never()).jsonToObject(anyString(), any(TypeReference.class));
		verify(jsonUtil, never()).ObjectToJson(any());
	}

	@SuppressWarnings("unchecked")
	@Test
	void testSendSearchAutoCompleteRequest_emptyKeyword_buildsCorrectEndpoint() 
	{
		WeatherApiMethod apiMethod = WeatherApiMethod.SEARCH;
		String emptyKeyword = "";
		String expectedEndpoint = BASE_URL + apiMethod.getValue() + "?key=" + API_KEY + "&q=" + emptyKeyword + "&aqi=no";
		
		when(properties.getUrl()).thenReturn(BASE_URL);
		when(properties.getKey()).thenReturn(API_KEY);
		when(searchAutoCompleteClient.sendSearchLocationsRequest(HttpMethod.GET, expectedEndpoint))
				.thenReturn(RAW_RESPONSE);
		when(jsonUtil.jsonToObject(anyString(), any(TypeReference.class))).thenReturn(MOCK_LOCATIONS);
		when(jsonUtil.ObjectToJson(MOCK_LOCATIONS)).thenReturn(PROCESSED_RESPONSE);

		String result = requestHandler.sendSearchAutoCompleteRequest(emptyKeyword, apiMethod);

		assertEquals(PROCESSED_RESPONSE, result);
		verify(searchAutoCompleteClient).sendSearchLocationsRequest(HttpMethod.GET, expectedEndpoint);
	}
}
