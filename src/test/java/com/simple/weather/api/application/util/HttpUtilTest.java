package com.simple.weather.api.application.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HttpUtilTest
{
	@Test
	public void encodeString_WithSpaces()
	{
		assertEquals("key+word", HttpUtil.encodeString("key word"));
	}
	
	@Test
	public void encodeString_WithoutSpaces()
	{
		assertEquals("keyword", HttpUtil.encodeString("keyword"));
	}
}
