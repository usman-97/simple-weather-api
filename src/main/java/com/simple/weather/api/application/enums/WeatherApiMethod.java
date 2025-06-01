package com.simple.weather.api.application.enums;

public enum WeatherApiMethod
{
	CURRENT("current.json");

	private String value;
	
	private WeatherApiMethod(String value)
	{
		this.value = value;
	}
	
	public String getValue()
	{
		return value;
	}
}
