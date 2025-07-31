package com.simple.weather.api.application.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonUtil
{
	private final ObjectMapper objectMapper;
	
	public <T> T jsonToObject(String json, Class<T> cls)
	{
		T object = null;
		
		try
		{
			object = objectMapper.readValue(json, cls);
		}
		catch (JsonProcessingException e)
		{
			log.error("Error converting json to object: {}", e.getMessage(), e);
		}
		
		return object;
	}
	
	public <T> T jsonToObject(String json, TypeReference<T> typeReference)
	{
		T object = null;
		
		try
		{
			object = objectMapper.readValue(json, typeReference);
		}
		catch (JsonProcessingException e)
		{
			log.error("Error converting json to object: {}", e.getMessage(), e);
		}
		
		return object;
	}
	
	public String ObjectToJson(Object object)
	{
		String json = null;
		
		try
		{
			json = objectMapper.writeValueAsString(object);
		}
		catch (JsonProcessingException e)
		{
			log.error("Error converting object to json: {}", e.getMessage(), e);
		}
		
		return json;
	}
}
