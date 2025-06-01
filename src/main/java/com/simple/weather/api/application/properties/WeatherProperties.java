package com.simple.weather.api.application.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "weather.api")
@Getter
@Setter
public class WeatherProperties
{
	private String url;
	private String key;
}
