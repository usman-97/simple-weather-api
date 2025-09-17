package com.simple.weather.api.application.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import io.jsonwebtoken.Jwts;

@ExtendWith(MockitoExtension.class)
class SecurityUtilTest
{
	@InjectMocks
	private SecurityUtil util;
	
	@Test
	public void generateKey()
	{
		assertNotNull(util.generateKey(Jwts.SIG.HS512));
	}
}
