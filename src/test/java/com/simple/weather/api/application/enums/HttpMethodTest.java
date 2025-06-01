package com.simple.weather.api.application.enums;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HttpMethodTest
{
	@Test
	public void testLength()
	{
		assertEquals(4, HttpMethod.values().length);
	}
}
