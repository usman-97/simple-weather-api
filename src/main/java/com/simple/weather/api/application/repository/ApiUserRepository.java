package com.simple.weather.api.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simple.weather.api.application.model.entity.ApiUser;

public interface ApiUserRepository extends JpaRepository<ApiUser, Long>
{
	public ApiUser findByClientId(String clientId);
}
