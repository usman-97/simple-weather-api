package com.simple.weather.api.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Location
{
	@JsonProperty("name")
	private String city;
	@JsonProperty("country")
	private String country;
	@JsonProperty("localtime")
	private String localTime;
}
