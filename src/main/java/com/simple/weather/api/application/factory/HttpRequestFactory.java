package com.simple.weather.api.application.factory;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.springframework.stereotype.Component;

import com.simple.weather.api.application.enums.HttpMethod;

@Component
public class HttpRequestFactory
{
	public HttpUriRequestBase createRequest(HttpMethod method, String endpoint)
	{
		HttpUriRequestBase request = null;
		
		switch (method)
		{
			case GET:
				request = new HttpGet(endpoint);
				break;
			default:
				break;
		}
		
		return request;
	}
}
