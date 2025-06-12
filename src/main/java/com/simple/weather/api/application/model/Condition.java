package com.simple.weather.api.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Condition
{
	@JsonProperty("code")
	private Integer code;
	@JsonProperty("text")
	private String weatherCondition;
	@JsonProperty("icon")
	private String iconUrl;
}
