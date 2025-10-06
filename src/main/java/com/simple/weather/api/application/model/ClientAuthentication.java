package com.simple.weather.api.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClientAuthentication
{
	@JsonProperty("clientId")
	private String clientId;
}
