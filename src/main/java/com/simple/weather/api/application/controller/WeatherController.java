package com.simple.weather.api.application.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simple.weather.api.application.processor.WeatherProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/weather")
@RequiredArgsConstructor
public class WeatherController extends ControllerBase
{
	private final WeatherProcessor processor;
	
	@GetMapping(value = "/details/{keyword}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> processWeatherDetails(@PathVariable final String keyword)
	{
		log.info("Getting weather data with keyword {}", keyword);
		String weatherData = processor.process(keyword);
		return buildResponse(weatherData);
	}
	
	@GetMapping(value = "/search/{keyword}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> processMatchCitiesForSearchedKeyword(@PathVariable final String keyword)
	{
		log.info("Getting search result for keyword {}.", keyword);
		String matchedCities = processor.processMatchedCities(keyword);
		return buildResponse(matchedCities);
	}
}
