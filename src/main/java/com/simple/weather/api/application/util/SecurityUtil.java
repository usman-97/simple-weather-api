package com.simple.weather.api.application.util;

import javax.crypto.SecretKey;

import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.MacAlgorithm;

public class SecurityUtil
{
	public String generateKey(MacAlgorithm algorithm)
	{
		SecretKey key = algorithm.key().build();
		return  Encoders.BASE64.encode(key.getEncoded());
	}
}
