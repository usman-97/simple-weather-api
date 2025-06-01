package com.simple.weather.api.application.client;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simple.weather.api.application.handler.response.ResponseHandlerBase;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public abstract class HTTPClientBase
{
	private final CloseableHttpClient httpClient;
	
	@Autowired
	public HTTPClientBase(final CloseableHttpClient client)
	{
		this.httpClient = client;
	}
	
	protected String sendRequest(final HttpUriRequestBase request, ResponseHandlerBase responseHandler)
	{
		String response = null;
		
		try
		{
			response = httpClient.execute(request, responseHandler);
		}
		catch (IOException e)
		{
			log.error("Error sending http request: {}", e.getMessage(), e);
		}
		
		return response;
	}
}
