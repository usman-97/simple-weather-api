package com.simple.weather.api.application.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JwtTokenResponse
{
	private String token;
	private long expiresIn;
}
