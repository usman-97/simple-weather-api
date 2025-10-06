package com.simple.weather.api.application.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.simple.weather.api.application.model.entity.ApiUser;
import com.simple.weather.api.application.repository.ApiUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApiUserService
{
	private final ApiUserRepository repository;
	
	public ApiUser fetchApiUser(String clientId)
	{
		ApiUser user = null;
		
		if (!StringUtils.hasText(clientId))
		{
			log.info("Failed to fetch api user because client id was null or empty.");
		}
		else
		{
			user = repository.findByClientId(clientId);
		}
		
		return user;
	}
}
