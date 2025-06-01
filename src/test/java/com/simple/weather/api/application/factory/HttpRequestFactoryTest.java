package com.simple.weather.api.application.factory;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simple.weather.api.application.enums.HttpMethod;

@ExtendWith(MockitoExtension.class)
public class HttpRequestFactoryTest {

	private static final String TEST_ENDPOINT = "http://api.example.com/resource";
	
	private final HttpRequestFactory httpRequestFactory = new HttpRequestFactory();

	@Test
	public void testCreateRequest_withGetMethod_shouldReturnHttpGet() throws URISyntaxException
	{
		HttpUriRequestBase request = httpRequestFactory.createRequest(HttpMethod.GET, TEST_ENDPOINT);
		assertNotNull(request);
		assertTrue(request instanceof HttpGet);
		assertEquals(TEST_ENDPOINT, request.getUri().toString());
	}

	@Test
	public void testCreateRequest_withPostMethod_shouldReturnNull()
	{
		HttpUriRequestBase request = httpRequestFactory.createRequest(HttpMethod.POST, TEST_ENDPOINT);
		assertNull(request);
	}

	@Test
	public void testCreateRequest_withPutMethod_shouldReturnNull()
	{
		HttpUriRequestBase request = httpRequestFactory.createRequest(HttpMethod.PUT, TEST_ENDPOINT);
		assertNull(request);
	}

	@Test
	public void testCreateRequest_withDeleteMethod_shouldReturnNull()
	{
		HttpUriRequestBase request = httpRequestFactory.createRequest(HttpMethod.DELETE, TEST_ENDPOINT);
		assertNull(request);
	}

	@Test
	public void testCreateRequest_withNullMethod_shouldThrowNpe()
	{
		assertThrows(NullPointerException.class, () -> 
			httpRequestFactory.createRequest(null, TEST_ENDPOINT));
	}

	@Test
	public void testCreateRequest_withEmptyEndpoint_shouldCreateRequest() throws URISyntaxException
	{
		HttpUriRequestBase request = httpRequestFactory.createRequest(HttpMethod.GET, "");
		assertNotNull(request);
		assertTrue(request instanceof HttpGet);
		assertEquals("/", request.getUri().toString());
	}

	@Test
	public void testCreateRequest_withNullEndpoint_shouldThrowNpe()
	{
		assertThrows(NullPointerException.class, () -> 
			httpRequestFactory.createRequest(HttpMethod.GET, null));
	}
}
