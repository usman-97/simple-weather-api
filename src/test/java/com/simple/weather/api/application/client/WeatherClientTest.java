package com.simple.weather.api.application.client;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simple.weather.api.application.enums.HttpMethod;
import com.simple.weather.api.application.factory.HttpRequestFactory;
import com.simple.weather.api.application.handler.response.ResponseHandlerBase;
import com.simple.weather.api.application.handler.response.WeatherResponseHandler;

@ExtendWith(MockitoExtension.class)
public class WeatherClientTest
{
	private static final String WEATHER_DATA_RESPONSE = "Weather data response";
	private static final String TEST_ENDPOINT = "http://api.weather.com/test";
	
	@Mock
	private CloseableHttpClient httpClient;
	@Mock
	private HttpRequestFactory httpRequestFactory;
	@Mock
	private WeatherResponseHandler responseHandler;
	@InjectMocks
	private WeatherClient weatherClient;

	@Test
	public void sendCurrentWeatherRequest_SuccessfulRequest_ReturnsResponse() throws Exception
	{
		when(httpRequestFactory.createRequest(HttpMethod.GET, TEST_ENDPOINT)).thenReturn(new HttpGet(TEST_ENDPOINT));
		when(httpClient.execute(any(), any(ResponseHandlerBase.class))).thenReturn(WEATHER_DATA_RESPONSE);

		String actualResponse = weatherClient.sendCurrentWeatherRequest(HttpMethod.GET, TEST_ENDPOINT);

		assertEquals(WEATHER_DATA_RESPONSE, actualResponse);
		
		verify(httpRequestFactory).createRequest(HttpMethod.GET, TEST_ENDPOINT);
		verify(httpClient).execute(any(), any(ResponseHandlerBase.class));
	}

	@Test
	public void sendCurrentWeatherRequest_RequestFactoryThrowsException_ThrowsRuntimeException()
	{
		when(httpRequestFactory.createRequest(HttpMethod.GET, TEST_ENDPOINT))
			.thenThrow(new RuntimeException("Request creation failed"));

		assertThrows(RuntimeException.class, () -> 
			weatherClient.sendCurrentWeatherRequest(HttpMethod.GET, TEST_ENDPOINT));
		
		verify(httpRequestFactory).createRequest(HttpMethod.GET, TEST_ENDPOINT);
		verifyNoInteractions(httpClient);
	}

	@Test
	public void sendCurrentWeatherRequest_HttpClientThrowsException_ThrowsRuntimeException() throws Exception
	{
		when(httpClient.execute(any(), any(ResponseHandlerBase.class)))
			.thenThrow(new RuntimeException("HTTP execution failed"));

		assertThrows(RuntimeException.class, () -> 
			weatherClient.sendCurrentWeatherRequest(HttpMethod.GET, TEST_ENDPOINT));
		
		verify(httpRequestFactory).createRequest(HttpMethod.GET, TEST_ENDPOINT);
		verify(httpClient).execute(any(), any(ResponseHandlerBase.class));
	}
	
	@Test
	public void sendCurrentWeatherRequest_HttpClientThrowsException_IOException() throws IOException
	{
		when(httpClient.execute(any(), any(ResponseHandlerBase.class)))
			.thenThrow(IOException.class);

		assertNull(weatherClient.sendCurrentWeatherRequest(HttpMethod.GET, TEST_ENDPOINT));
		
		verify(httpRequestFactory).createRequest(HttpMethod.GET, TEST_ENDPOINT);
		verify(httpClient).execute(any(), any(ResponseHandlerBase.class));
	}

	@Test
	public void sendCurrentWeatherRequest_NonGetMethod_ThrowsClassCastException()
	{
		HttpMethod postMethod = HttpMethod.POST;
		
		when(httpRequestFactory.createRequest(postMethod, TEST_ENDPOINT))
			.thenReturn(mock(HttpPost.class)); // Assuming it returns HttpPost

		assertThrows(ClassCastException.class, () -> 
			weatherClient.sendCurrentWeatherRequest(postMethod, TEST_ENDPOINT));
		
		verify(httpRequestFactory).createRequest(postMethod, TEST_ENDPOINT);
		verifyNoInteractions(httpClient);
	}
}
