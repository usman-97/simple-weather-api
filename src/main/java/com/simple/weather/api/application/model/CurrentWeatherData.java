package com.simple.weather.api.application.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CurrentWeatherData
{
	@JsonProperty("temp_c")
	private BigDecimal temperature;
	@JsonProperty("is_day")
	private Integer isDay;
	@JsonProperty("condition")
	private Condition condition;
	@JsonProperty("wind_kph")
	private BigDecimal windSpeedInKph;
	@JsonProperty("humidity")
	private BigDecimal humidity;
	@JsonProperty("feelslike_c")
	private BigDecimal temperatureFeelsLike;
}
