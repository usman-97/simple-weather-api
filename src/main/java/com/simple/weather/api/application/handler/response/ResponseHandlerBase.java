package com.simple.weather.api.application.handler.response;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.springframework.stereotype.Component;

@Component
public abstract class ResponseHandlerBase implements HttpClientResponseHandler<String>
{
	@Override
	public String handleResponse(ClassicHttpResponse response)
	{
		return "";
	}
}
