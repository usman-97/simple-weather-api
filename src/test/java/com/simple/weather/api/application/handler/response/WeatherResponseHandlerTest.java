package com.simple.weather.api.application.handler.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class WeatherResponseHandlerTest 
{
	@Mock
	private ClassicHttpResponse response;
	@InjectMocks
	private WeatherResponseHandler weatherResponseHandler;

	@Test
	public void testHandleResponse_successfulResponse() throws IOException, ParseException 
	{
		String expectedResponse = "{\"temperature\":72}";
		StringEntity entity = new StringEntity(expectedResponse);
		
		when(response.getCode()).thenReturn(HttpStatus.OK.value());
		when(response.getEntity()).thenReturn(entity);
		
		String result = weatherResponseHandler.handleResponse(response);
		
		assertEquals(expectedResponse, result);
	}

	@Test
	public void testHandleResponse_non200Response() 
	{
		when(response.getCode()).thenReturn(HttpStatus.BAD_REQUEST.value());
		
		String result = weatherResponseHandler.handleResponse(response);
		
		assertEquals("", result);
	}

	@Test
	public void testHandleResponse_parseException() throws ParseException, IOException 
	{
		StringEntity entity = new StringEntity("");
		
		when(response.getCode()).thenReturn(HttpStatus.OK.value());
		when(response.getEntity()).thenReturn(entity);
		
		String result = weatherResponseHandler.handleResponse(response);
		
		assertEquals("", result);
	}

	@Test
	public void testHandleResponse_ioException() throws ParseException, IOException 
	{
		IOException mockException = new IOException("Network error");
		
		when(response.getCode()).thenReturn(HttpStatus.OK.value());
		when(response.getEntity()).thenReturn(mock(HttpEntity.class));
		when(EntityUtils.toString(response.getEntity())).thenThrow(mockException);
		
		String result = weatherResponseHandler.handleResponse(response);
		
		assertEquals("", result);
	}
}