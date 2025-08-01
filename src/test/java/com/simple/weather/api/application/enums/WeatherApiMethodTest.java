package com.simple.weather.api.application.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class WeatherApiMethodTest
{
	@Test
	public void testLength()
	{
		assertEquals(2, WeatherApiMethod.values().length);
	}
	
	@Test
	public void testGetValue_CURRENT()
	{
		assertEquals("current.json", WeatherApiMethod.CURRENT.getValue());
	}
}
