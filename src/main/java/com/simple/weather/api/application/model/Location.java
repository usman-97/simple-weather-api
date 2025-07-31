package com.simple.weather.api.application.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location
{
	@JsonProperty("name")
	private String city;
	@JsonProperty("country")
	private String country;
	@JsonProperty("localtime")
	private String localTime;
}
