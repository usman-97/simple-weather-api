package com.simple.weather.api.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
	private static final String VALID_KEYWORD = "london";
	private static final String EMPTY_KEYWORD = "";
	private static final String EXPECTED_RESULT = "{London}";
	
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
	
	@Test
	void testProcessMatchCitiesForSearchedKeyword_validKeyword_returnsOkWithResults() 
	{
		when(processor.processMatchedCities(VALID_KEYWORD)).thenReturn(EXPECTED_RESULT);
		
		ResponseEntity<?> response = controller.processMatchCitiesForSearchedKeyword(VALID_KEYWORD);
		
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(EXPECTED_RESULT, response.getBody());
		verify(processor).processMatchedCities(VALID_KEYWORD);
	}

	@Test
	void testProcessMatchCitiesForSearchedKeyword_emptyKeyword_returnsOkWithEmptyResult() 
	{
		when(processor.processMatchedCities(EMPTY_KEYWORD)).thenReturn(EMPTY_KEYWORD);
		
		ResponseEntity<?> response = controller.processMatchCitiesForSearchedKeyword(EMPTY_KEYWORD);
		
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("", response.getBody());
	}

	@Test
	void testProcessMatchCitiesForSearchedKeyword_verifySingleProcessorCall() 
	{
		controller.processMatchCitiesForSearchedKeyword(VALID_KEYWORD);
		
		verify(processor, times(1)).processMatchedCities(VALID_KEYWORD);
		verifyNoMoreInteractions(processor);
	}
}
