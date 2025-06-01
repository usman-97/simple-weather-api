package com.simple.weather.api.application.handler.request;

import org.springframework.stereotype.Component;

import com.simple.weather.api.application.client.WeatherClient;
import com.simple.weather.api.application.enums.HttpMethod;
import com.simple.weather.api.application.enums.WeatherApiMethod;
import com.simple.weather.api.application.model.CurrentWeatherData;
import com.simple.weather.api.application.model.WeatherData;
import com.simple.weather.api.application.properties.WeatherProperties;
import com.simple.weather.api.application.util.JsonUtil;
import com.simple.weather.api.application.util.MathUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WeatherRequestHandler
{
	private final WeatherClient client;
	private final WeatherProperties properties;
	private final JsonUtil jsonUtil;
	
	/**
	 * Send request to external weather api to get current weather data and then filter out weather data with only
	 * required fields
	 * 
	 * @param keyword
	 * 			Keyword could be city, post code, etc.
	 * @param apiMethod
	 * 			External weather api method
	 * @return	Json with required weather fields
	 */
	public String sendCurrentWeatherRequest(String keyword, WeatherApiMethod apiMethod)
	{
		String endpoint = properties.getUrl() + apiMethod.getValue() + "?key=" + properties.getKey() + "&q=" + keyword
				+ "&aqi=no";
		String response = client.sendCurrentWeatherRequest(HttpMethod.GET, endpoint);
		if (response == null)
		{
			return response;
		}
		
		WeatherData weatherData = jsonUtil.jsonToObject(response, WeatherData.class);
		roundTemperatureValues(weatherData);
		return jsonUtil.ObjectToJson(weatherData);
	}
	
	private void roundTemperatureValues(WeatherData weatherData)
	{
		if (weatherData == null)
		{
			return;
		}
		
		CurrentWeatherData current = weatherData.getWeatherData();
		if (current == null)
		{
			return;
		}
		
		current.setTemperature(MathUtil.safeRoundToNearestWhole(current.getTemperature()));
		current.setTemperatureFeelsLike(MathUtil.safeRoundToNearestWhole(current.getTemperatureFeelsLike()));
		current.setWindSpeedInKph(MathUtil.safeRoundToNearestWhole(current.getWindSpeedInKph()));
		current.setHumidity(MathUtil.safeRoundToNearestWhole(current.getHumidity()));
	}
}
