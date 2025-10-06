package com.simple.weather.api.application.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public final class HttpUtil
{
	public static String encodeString(String key)
	{
		String encodedKey = "";
		
		try
		{
			encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e)
		{
			log.error("Error encoding {}: {}", key, e.getMessage(), e);
		}
		
		return encodedKey;
	}
}
