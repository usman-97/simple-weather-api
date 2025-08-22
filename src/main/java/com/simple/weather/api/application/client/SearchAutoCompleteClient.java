package com.simple.weather.api.application.client;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simple.weather.api.application.enums.HttpMethod;
import com.simple.weather.api.application.factory.HttpRequestFactory;
import com.simple.weather.api.application.handler.response.WeatherResponseHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SearchAutoCompleteClient extends HTTPClientBase
{
	private final HttpRequestFactory httpRequestFactory;
	private final WeatherResponseHandler responseHandler;
	
	@Autowired
	public SearchAutoCompleteClient(CloseableHttpClient client, WeatherResponseHandler responseHandler)
	{
		super(client);
		this.httpRequestFactory = new HttpRequestFactory();
		this.responseHandler = new WeatherResponseHandler();
	}

	public String sendSearchLocationsRequest(HttpMethod method, String endpoint)
	{
		log.info("Sending request to {}", endpoint);
		HttpGet request = (HttpGet) httpRequestFactory.createRequest(method, endpoint);
		return sendRequest(request, responseHandler);
	}
}
