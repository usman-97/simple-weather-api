package com.simple.weather.api.application.handler.response;

import java.io.IOException;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WeatherResponseHandler extends ResponseHandlerBase
{
	@Override
	public String handleResponse(ClassicHttpResponse response)
	{
		String responseStr = "";
		
		if (response.getCode() == HttpStatus.OK.value())
		{
			try
			{
				responseStr = EntityUtils.toString(response.getEntity());
				log.info("Response: {}", responseStr);
			}
			catch (ParseException | IOException e)
			{
				log.error("Error getting response: {}", e.getMessage(), e);
			}
		}
		
		return responseStr;
	}
}
