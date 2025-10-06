package com.simple.weather.api.application.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T>
{
	private boolean success;
	private String message;
	private T data;
}
