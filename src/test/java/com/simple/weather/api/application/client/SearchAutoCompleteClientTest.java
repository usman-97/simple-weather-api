package com.simple.weather.api.application.client;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simple.weather.api.application.enums.HttpMethod;
import com.simple.weather.api.application.factory.HttpRequestFactory;
import com.simple.weather.api.application.handler.response.ResponseHandlerBase;
import com.simple.weather.api.application.handler.response.WeatherResponseHandler;

@ExtendWith(MockitoExtension.class)
class SearchAutoCompleteClientTest {

	@Mock
	private CloseableHttpClient httpClient;

	@Mock
	private WeatherResponseHandler responseHandler;

	@Mock
	private HttpRequestFactory httpRequestFactory;

	@Mock
	private CloseableHttpResponse httpResponse;

	@InjectMocks
	private SearchAutoCompleteClient searchAutoCompleteClient;

	private static final String TEST_ENDPOINT = "https://api.weather.com/search?q=lon";
	private static final String EXPECTED_RESPONSE = "{\"locations\":[]}";

	@Test
	void testSendSearchLocationsRequest_validGetRequest_returnsResponse() 
		throws Exception 
	{
		when(httpClient.execute(any(), any(ResponseHandlerBase.class))).thenReturn(EXPECTED_RESPONSE);

		String result = searchAutoCompleteClient.sendSearchLocationsRequest(HttpMethod.GET, TEST_ENDPOINT);

		assertEquals(EXPECTED_RESPONSE, result);
	}

	@Test
	void testSendSearchLocationsRequest_handlerReturnsNull_returnsNull() 
		throws Exception 
	{
		String result = searchAutoCompleteClient.sendSearchLocationsRequest(HttpMethod.GET, TEST_ENDPOINT);

		assertNull(result);
	}
}