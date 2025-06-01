package com.simple.weather.api.application.util;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class JsonUtilTest 
{
	@Mock
	private ObjectMapper objectMapper;
	@InjectMocks
	private JsonUtil jsonUtil;

	@Test
	public void testJsonToObject_success() throws JsonProcessingException 
	{
		String json = "{\"name\":\"John\"}";
		TestData expected = new TestData("John");
		
		when(objectMapper.readValue(json, TestData.class)).thenReturn(expected);
		
		TestData result = jsonUtil.jsonToObject(json, TestData.class);
		
		assertEquals(expected, result);
	}

	@Test
	public void testJsonToObject_failure() throws JsonProcessingException 
	{
		String invalidJson = "invalid";
		JsonProcessingException mockException = mock(JsonProcessingException.class);
		
		when(objectMapper.readValue(invalidJson, TestData.class)).thenThrow(mockException);
		when(mockException.getMessage()).thenReturn("Parsing error");
		
		TestData result = jsonUtil.jsonToObject(invalidJson, TestData.class);
		
		assertNull(result);
	}

	@Test
	public void testObjectToJson_success() throws JsonProcessingException 
	{
		TestData input = new TestData("Alice");
		String expectedJson = "{\"name\":\"Alice\"}";
		
		when(objectMapper.writeValueAsString(input)).thenReturn(expectedJson);
		
		String result = jsonUtil.ObjectToJson(input);
		
		assertEquals(expectedJson, result);
	}

	@Test
	public void testObjectToJson_failure() throws JsonProcessingException 
	{
		TestData input = new TestData("Bob");
		JsonProcessingException mockException = mock(JsonProcessingException.class);
		
		when(objectMapper.writeValueAsString(input)).thenThrow(mockException);
		when(mockException.getMessage()).thenReturn("Serialization error");
		
		String result = jsonUtil.ObjectToJson(input);
		
		assertNull(result);
	}

	@Test
	public void testJsonToObject_nullInput() 
	{
		TestData result = jsonUtil.jsonToObject(null, TestData.class);
		assertNull(result);
	}

	@Test
	public void testObjectToJson_nullInput() 
	{
		String result = jsonUtil.ObjectToJson(null);
		assertNull(result);
	}

	// Helper class for testing
	private static class TestData 
	{
		private String name;

		public TestData(String name) 
		{
			this.name = name;
		}

		// equals() and hashCode() needed for assertions
		@Override
		public boolean equals(Object o) 
		{
			if (this == o)
			{
				return true;
			}
			if (o == null || getClass() != o.getClass())
			{
				return false;
			}
			TestData testData = (TestData) o;
			return name.equals(testData.name);
		}

		@Override
		public int hashCode() 
		{
			return name.hashCode();
		}
	}
}
